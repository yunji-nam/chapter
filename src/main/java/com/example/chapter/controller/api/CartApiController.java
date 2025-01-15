package com.example.chapter.controller.api;

import com.example.chapter.dto.CartDto;
import com.example.chapter.dto.api.ApiResponse;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cart")
public class CartApiController {

    private final CartService cartService;

    @PostMapping
    public ApiResponse<String> addCartItem(@Valid @RequestBody CartDto dto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.addCartItem(dto, userDetails.getUser());
        return new ApiResponse<>("장바구니 추가 완료");
    }

    @PutMapping("/items")
    public ApiResponse<String> updateCart(@Valid @RequestBody CartDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.updateCartItemQuantity(dto, userDetails.getUser());
        return new ApiResponse<>("장바구니 수량 변경 완료");
    }

    @DeleteMapping
    public ApiResponse<String> deleteCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteCart(userDetails.getUser());
        return new ApiResponse<>("장바구니 상품 전체 삭제 완료");
    }

    @DeleteMapping("/items")
    public ApiResponse<String> deleteItemsFromCart(@RequestParam List<Long> cartItemIds, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteItemsFromCart(cartItemIds, userDetails.getUser());
        return new ApiResponse<>("장바구니 상품 삭제 완료");
    }
}
