package com.example.chapter.service;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.PaymentCallbackDto;
import com.example.chapter.dto.PaymentPrepareDto;
import com.example.chapter.entity.*;
import com.example.chapter.exception.PaymentAmountMismatchException;
import com.example.chapter.repository.CartItemRepository;
import com.example.chapter.repository.OrderRepository;
import com.example.chapter.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    // 사전 검증
    public ApiResponse<String> preparePayment(PaymentPrepareDto prepareDto) throws IamportResponseException, IOException {
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

    public ApiResponse<String> completePayment(String merchantUid, User user, OrderRequestDto dto) {
        List<Long> cartItemIds = dto.getCartItemIds();
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        Address address = dto.getDeliveryAddress();
        String name = dto.getDeliveryName();
        String phone = dto.getDeliveryPhone();
        Delivery delivery = new Delivery(address, name, phone);

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order(merchantUid, user, delivery, orderItems);
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();

            order.addOrderItem(book, quantity);
        }
        orderRepository.save(order);

        BigDecimal amount = dto.getAmount();
        String payMethod = dto.getPayMethod();

        PaymentEntity payment = new PaymentEntity(amount, payMethod, order);
        paymentRepository.save(payment);

        return new ApiResponse<>("결제 완료");
    }
}
