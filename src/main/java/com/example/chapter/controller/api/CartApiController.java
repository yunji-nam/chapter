package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.CartDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartApiController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse> addCartItem(@Valid @RequestBody CartDto dto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.addCartItem(dto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponse("장바구니 추가 완료"));
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse> updateCart(@Valid @RequestBody CartDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.updateCartItemQuantity(dto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponse("장바구니 수량 업데이트 완료"));
    }
}
