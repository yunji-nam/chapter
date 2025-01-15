package com.example.chapter.controller;

import com.example.chapter.dto.CartItemDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartItemDto> cartItems = cartService.getCart(userDetails.getUser());
        model.addAttribute("cartList", cartItems);
        return "user/cart";
    }

}
