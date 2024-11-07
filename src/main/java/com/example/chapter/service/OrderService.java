package com.example.chapter.service;

import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderListDto;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.entity.*;
import com.example.chapter.repository.CartRepository;
import com.example.chapter.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    // 주문
    public void createOrder(User user, OrderRequestDto dto) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("cart를 찾을 수 없습니다."));
        List<CartItem> items = cart.getItems();

        List<OrderItem> orderItems = new ArrayList<>();

        Address address = dto.getDeliveryAddress();
        String name = dto.getDeliveryName();
        String phone = dto.getDeliveryPhone();

        Delivery delivery = new Delivery(address, name, phone);

        Order order = new Order(user, delivery, orderItems);

        for (CartItem cartItem : items) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();

            order.addOrderItem(book, quantity);
        }

        orderRepository.save(order);
    }

    // 주문 조회
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

    private static void checkUser(User user, Order order) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("유저가 다릅니다.");
        }
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
    }
}
