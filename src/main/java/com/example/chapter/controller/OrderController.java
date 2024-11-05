package com.example.chapter.controller;

import com.example.chapter.dto.CartItemDto;
import com.example.chapter.dto.OrderDetailDto;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import com.example.chapter.service.OrderService;
import com.example.chapter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping
    public String orderForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        List<CartItemDto> cartItems = cartService.getCart(user);
        model.addAttribute("form", new OrderRequestDto(user, cartItems));

        return "order/order";
    }

    @PostMapping
    public String order(@AuthenticationPrincipal UserDetailsImpl userDetails,
                        @Valid OrderRequestDto dto) {
        orderService.createOrder(userDetails.getUser(), dto);
        return "redirect:/orders";
    }

    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        OrderDetailDto dto = orderService.getOrder(orderId, userDetails.getUser());
        model.addAttribute("order", dto);

        return "order/detail";
    }


}
