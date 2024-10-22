package com.example.chapter.controller;

import com.example.chapter.dto.CartDto;
import com.example.chapter.dto.CartItemDto;
import com.example.chapter.dto.CartUpdateDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public String addCartItem(@Valid CartDto dto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestParam("redirectUrl") String redirectUrl) {
        cartService.addCartItem(dto, userDetails.getUser());
        return "redirect:" + redirectUrl;
    }

    @GetMapping
    public String getCart(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<CartItemDto> cartItemDtos = cartService.getCart(userDetails.getUser());
        model.addAttribute("cartList", cartItemDtos);
        return "user/cart";
    }

    @PutMapping("/items/quantity")
    public String updateCart(@Valid CartUpdateDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.updateCartItemQuantity(dto, userDetails.getUser());
        return "redirect:/user/cart";
    }

    @DeleteMapping("/items")
    public String deleteCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteCart(userDetails.getUser());
        return "redirect:/user/cart";
    }

    @DeleteMapping("/items/{cartItemId}")
    public String deleteItemFromCart(@PathVariable Long cartItemId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteItemFromCart(cartItemId, userDetails.getUser());
        return "redirect:/user/cart";
    }




}
