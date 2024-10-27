package com.example.chapter.service;

import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.OrderResponseDto;
import com.example.chapter.entity.*;
import com.example.chapter.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    //주문
    public void createOrder(User user, OrderRequestDto dto) {
        List<OrderBook> orderBooks = new ArrayList<>();

        Cart cart = user.getCart();
        List<CartItem> items = cart.getItems();

        for (CartItem cartItem : items) {
            Book book = cartItem.getBook();
            int quantity = cartItem.getQuantity();

            OrderBook orderBook = new OrderBook(book, quantity);
            orderBooks.add(orderBook);
        }

        Address address = user.getAddress();
        Delivery delivery = new Delivery(address);

        Order order = new Order(user, delivery, orderBooks);
        orderRepository.save(order);
    }

    //주문 조회
    public OrderResponseDto getOrder(Long id, User user) {
        Order order = findOrder(id);
        checkUser(user, order);
        return new OrderResponseDto(order);
    }

    //주문 취소
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
