package com.example.chapter.service;

import com.example.chapter.dto.OrderItemDto;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.PaymentCallbackDto;
import com.example.chapter.dto.PaymentDto;
import com.example.chapter.dto.api.ApiResponse;
import com.example.chapter.entity.*;
import com.example.chapter.exception.PaymentAmountMismatchException;
import com.example.chapter.repository.OrderRepository;
import com.example.chapter.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chapter.constant.CacheConstant.BOOK_LIST;
import static com.example.chapter.constant.CacheConstant.FEATURED_BOOK_LIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderService orderService;
    private final BookService bookService;

    // 사전 검증
    public ApiResponse<String> preparePayment(PaymentDto prepareDto) throws IamportResponseException, IOException {
        BigDecimal amount = prepareDto.getAmount();
        PrepareData prepareData = new PrepareData(prepareDto.getMerchant_uid(), amount);
        iamportClient.postPrepare(prepareData);
        log.info("사전 검증 완료");
        return new ApiResponse<>("payment 사전 검증 완료");
    }

    // 사후 검증
    public IamportResponse<Payment> validatePayment(PaymentCallbackDto callbackDto) throws IamportResponseException, IOException {
        // 결제 금액 확인
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(callbackDto.getImpUid());
        BigDecimal amount = iamportResponse.getResponse().getAmount(); // 실제 결제 금액
        log.info("iam amount:{}", amount);
        BigDecimal requestPrice = BigDecimal.valueOf(callbackDto.getRequestPrice());
        log.info("request price:{}", requestPrice);

        if (!amount.equals(requestPrice)) {
            CancelData cancelData = createCancelData(iamportResponse);
            iamportClient.cancelPaymentByImpUid(cancelData);

            throw new PaymentAmountMismatchException("결제 금액 불일치");
        }

        return iamportResponse;
    }

    private CancelData createCancelData(IamportResponse<Payment> response) {
        return new CancelData(response.getResponse().getImpUid(), true);
    }

    @Transactional
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public ApiResponse<String> completePayment(String merchantUid, User user, OrderRequestDto dto) {
        List<OrderItemDto> items = dto.getOrderItems();
        Address address = new Address(dto.getDeliveryZipcode(), dto.getDeliveryStreet(), dto.getDeliveryDetail());
        String name = dto.getDeliveryName();
        String phone = dto.getDeliveryPhone();
        Delivery delivery = new Delivery(address, name, phone);

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order(merchantUid, user, delivery, orderItems);

        for (OrderItemDto item : items) {
            Long bookId = item.getBookId();
            int quantity = item.getQuantity();
            Book book = bookService.findBook(bookId);
            bookService.decreaseStock(book, quantity);
            order.addOrderItem(book, quantity);
        }
        orderRepository.save(order);

        BigDecimal amount = dto.getAmount();
        String payMethod = dto.getPayMethod();

        PaymentEntity payment = new PaymentEntity(amount, payMethod, order);
        paymentRepository.save(payment);

        return new ApiResponse<>("결제 완료");
    }

    private String prepareRefund(PaymentDto dto) throws IamportResponseException, IOException {
        Order order = orderService.findOrderByMerchantUid(dto.getMerchant_uid());
        PaymentEntity payment = paymentRepository.findByOrderId(order.getId()).orElseThrow(() ->
                new EntityNotFoundException("payment를 찾을 수 없습니다."));
        log.info("payment amount{}", payment.getAmount());
        log.info("request amount{}", dto.getAmount());

        if (payment.getAmount().compareTo(dto.getAmount()) != 0) {
            throw new IllegalArgumentException("잘못된 요청");
        }

        IamportResponse<AccessToken> accessTokenIamportResponse = iamportClient.getAuth();
        return accessTokenIamportResponse.getResponse().getToken();
    }

    public ResponseEntity<?> cancelPayment(PaymentDto dto) throws IamportResponseException, IOException {
        String iamportToken = prepareRefund(dto);

        String url = "https://api.iamport.kr/payments/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", iamportToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("merchant_uid", dto.getMerchant_uid());
        requestBody.put("amount", dto.getAmount());
        requestBody.put("reason", "cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

}
