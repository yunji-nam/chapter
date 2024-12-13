package com.example.chapter.service;

import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderListDto;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.User;
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

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final PaymentService paymentService;

    // 주문 번호 생성
    public String generateOrderNumber() {
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
        Order order = findOrderById(id);
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
        Order order = findOrderById(id);
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

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
    }
}
