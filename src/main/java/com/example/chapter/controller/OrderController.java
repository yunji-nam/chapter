package com.example.chapter.controller;

import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderFormDto;
import com.example.chapter.dto.OrderListDto;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 폼
    @GetMapping ("/order/form")
    public String orderForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        String merchantUid = orderService.generateOrderNumber();
        model.addAttribute("form", new OrderFormDto(merchantUid, user));
        model.addAttribute("merchantUid", merchantUid);
        return "order/order";
    }

    @PostMapping("/order/{orderId}")
    public String confirmOrder(@PathVariable Long orderId, @Valid OrderRequestDto dto) {
        orderService.confirmOrder(orderId, dto);
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
