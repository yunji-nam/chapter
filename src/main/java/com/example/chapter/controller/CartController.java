package com.example.chapter.controller;

import com.example.chapter.dto.CartItemDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartItemDto> cartItemDtos = cartService.getCart(userDetails.getUser());
        model.addAttribute("cartList", cartItemDtos);
        return "user/cart";
    }

    @DeleteMapping("/items")
    public String deleteCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteCart(userDetails.getUser());
        return "redirect:/cart";
    }

    @DeleteMapping("/items/{cartItemId}")
    public String deleteItemFromCart(@PathVariable Long cartItemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteItemFromCart(cartItemId, userDetails.getUser());
        return "redirect:/cart";
    }

}
