package com.example.chapter.service;

import com.example.chapter.dto.*;
import com.example.chapter.entity.*;
import com.example.chapter.repository.CartItemRepository;
import com.example.chapter.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    // 주문 준비
    public Long prepareOrder(User user, List<CartDto> cartList) {
        if (cartList == null || cartList.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        List<Long> cartItemIds = cartList.stream()
                .map(CartDto::getCartItemId).toList();
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
        String merchant_uid = generateOrderNumber();
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order(merchant_uid, user, orderItems);
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();

            order.addOrderItem(book, quantity);
        }
        orderRepository.save(order);
        return order.getId();
    }

    public List<OrderItemDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("order를 찾을 수 없습니다."));
        return order.getItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }

    // 주문 준비 취소
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    // 주문 확정
    public void confirmOrder(OrderRequestDto dto) {
        Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(() ->
                new EntityNotFoundException("order를 찾을 수 없습니다."));

        // 배송 정보 생성
        Address address = dto.getDeliveryAddress();
        String name = dto.getDeliveryName();
        String phone = dto.getDeliveryPhone();

        Delivery delivery = new Delivery(address, name, phone);

        order.confirmOrder(delivery);
    }

    public Order validOrder(String merchantUid) {
        return orderRepository.findByMerchantUid(merchantUid).orElseThrow(()
                -> new EntityNotFoundException("order를 찾을 수 없습니다."));
    }

    // 주문 번호 생성
    private String generateOrderNumber() {
        String randomString = generateRandomString(6);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        String formattedDay = now.format(dateTimeFormatter).replace("-", "");

        return formattedDay + randomString;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    // 주문 상세 조회
    public OrderDetailDto getOrder(Long id, User user) {
        Order order = findOrder(id);
        checkUser(user, order);
        return new OrderDetailDto(order);
    }

    // 주문 목록 조회 (최근 2년 내)
    public Page<OrderListDto> getOrders(User user, LocalDateTime startDate, LocalDateTime endDate, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        LocalDateTime twoYearAgo = LocalDateTime.now().minusYears(2);
        LocalDateTime now = LocalDateTime.now();

        if (startDate == null || startDate.isBefore(twoYearAgo)) {
            startDate = twoYearAgo;
        }
        if (endDate == null || endDate.isAfter(now)) {
            endDate = now;
        }

        Page<Order> page = orderRepository.findByUserIdAndOrderDateBetween(user.getId(), startDate, endDate, pageable);
        return page.map(OrderListDto::new);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long id, User user) {
        Order order = findOrder(id);
        checkUser(user, order);
        order.cancel();
    }

    public Page<OrderListDto> getAllOrders(LocalDateTime startDate, LocalDateTime endDate, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size);
        LocalDateTime twoYearAgo = LocalDateTime.now().minusYears(2);
        LocalDateTime now = LocalDateTime.now();

        if (startDate == null || startDate.isBefore(twoYearAgo)) {
            startDate = twoYearAgo;
        }
        if (endDate == null || endDate.isAfter(now)) {
            endDate = now;
        }

        Page<Order> page = orderRepository.findByOrderDateBetween(startDate, endDate, pageable);
        return page.map(OrderListDto::new);
    }

    private static void checkUser(User user, Order order) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("유저가 다릅니다.");
        }
    }

    public Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
    }
}
