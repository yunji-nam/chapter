package com.example.chapter.service;

import com.example.chapter.dto.OrderBookInfo;
import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderListDto;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.ReviewStatus;
import com.example.chapter.entity.User;
import com.example.chapter.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final ReviewService reviewService;

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
    public OrderDetailDto getOrder(Long orderId, User user) {
        Order order = findOrderById(orderId);
        checkUser(user, order);
        List<OrderBookInfo> books = order.getItems().stream().map(orderItem -> {
            ReviewStatus reviewStatus = reviewService.getReviewStatus(orderId, user, orderItem.getId());
            return new OrderBookInfo(orderItem, reviewStatus);
        }).toList();

        return new OrderDetailDto(order, books);
    }

    // 주문 목록 조회 (최근 2년 내)
    public Page<OrderListDto> getOrders(User user, LocalDate startDate, LocalDate endDate, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size);
        LocalDate twoYearAgo = LocalDate.now().minusYears(2);
        LocalDate now = LocalDate.now();

        if (startDate == null || startDate.isBefore(twoYearAgo)) {
            startDate = twoYearAgo;
        }
        if (endDate == null || endDate.isAfter(now)) {
            endDate = now;
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        Page<Order> page;
        if (user.isAdmin()) {
            page = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);
        } else {
            page = orderRepository.findByUserIdAndCreatedAtBetween(user.getId(), startDateTime, endDateTime, pageable);
        }

        return page.map(OrderListDto::new);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long id, User user) {
        Order order = findOrderById(id);
        checkUser(user, order);
        order.cancel();
    }

    public Page<OrderListDto> searchOrder(String query, String condition, Pageable pageable) {
        Page<Order> page;
        switch (condition) {
            case "merchantUid":
                page = orderRepository.findByMerchantUidContainingIgnoreCase(query, pageable);
                break;
            case "username":
                page = orderRepository.findByUser_NameContainingIgnoreCase(query, pageable);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 값입니다.");
        }
        return page.map(OrderListDto::new);
    }

    public List<OrderListDto> getLatestOrderList(User user) {
        List<Order> orderList = orderRepository.findTop5ByUserIdOrderByCreatedAtDesc(user.getId());
        return orderList.stream().map(OrderListDto::new).collect(Collectors.toList());
    }

    private PageRequest getPageable(int pageNo, int size) {
        return PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private static void checkUser(User user, Order order) {
        if (!user.isAdmin() && !order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
    }

    public Order findOrderByMerchantUid(String merchantUid) {
        return orderRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
    }

}
