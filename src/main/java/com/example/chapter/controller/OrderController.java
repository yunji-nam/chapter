package com.example.chapter.controller;

import com.example.chapter.dto.*;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // cart 선택 아이템 받기
    @PostMapping("/order/prepare")
    public ResponseEntity<Map<String, Object>> prepareOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestBody OrderPrepareDto dto) {
        Long orderId = orderService.prepareOrder(userDetails.getUser(), dto.getCartList());
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        return ResponseEntity.ok(response);
    }

    // 주문 폼
    @GetMapping ("/order/form")
    public String orderForm(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long orderId, Model model) {
        User user = userDetails.getUser();
        List<OrderItemDto> orderItems = orderService.getOrderItems(orderId);
        Order order = orderService.findOrder(orderId);
        model.addAttribute("form", new OrderFormDto(order, user, orderItems));

        return "order/order";
    }

    @PostMapping("/order/{orderId}")
    public String confirmOrder(@Valid OrderRequestDto dto) {
        orderService.confirmOrder(dto);
        return "redirect:/orders";
    }

    // 주문 조회
    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        OrderDetailDto dto = orderService.getOrder(orderId, userDetails.getUser());
        model.addAttribute("order", dto);

        return "order/detail";
    }

    // 주문 목록 조회
    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam(required = false) LocalDateTime startDate,
                                        @RequestParam(required = false) LocalDateTime endDate,
                                        @RequestParam(defaultValue = "0") int pageNo,
                                        @RequestParam(defaultValue = "2") int size,
                            Model model) {
        Page<OrderListDto> orders = orderService.getOrders(userDetails.getUser(), startDate, endDate, pageNo, size);

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "order/list";
    }

    // 주문 취소
    @DeleteMapping("/order/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderId, userDetails.getUser());
        return "redirect:/orders";
    }

}
