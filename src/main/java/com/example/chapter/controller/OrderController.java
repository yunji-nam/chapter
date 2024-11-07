package com.example.chapter.controller;

import com.example.chapter.dto.CartItemDto;
import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import com.example.chapter.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/order")
    public String orderForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        List<CartItemDto> cartItems = cartService.getCart(user);
        model.addAttribute("form", new OrderRequestDto(user, cartItems));

        return "order/order";
    }

    @PostMapping("/order")
    public String order(@AuthenticationPrincipal UserDetailsImpl userDetails,
                        @Valid OrderRequestDto dto) {
        orderService.createOrder(userDetails.getUser(), dto);
        return "redirect:/orders";
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        OrderDetailDto dto = orderService.getOrder(orderId, userDetails.getUser());
        model.addAttribute("order", dto);

        return "order/detail";
    }

    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestParam(required = false) LocalDateTime startDate,
                                        @RequestParam(required = false) LocalDateTime endDate,
                                        @RequestParam(defaultValue = "0") int pageNo,
                                        @RequestParam(defaultValue = "2") int size) {
        orderService.getOrders(userDetails.getUser(), startDate, endDate, pageNo, size);
        return "order/list";
    }

    @DeleteMapping("/order/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderId, userDetails.getUser());
        return "redirect:/orders";
    }

}
