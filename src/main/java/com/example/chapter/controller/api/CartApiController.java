package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.CartDto;
import com.example.chapter.dto.CartUpdateDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            cartService.addCartItem(dto, userDetails.getUser());
            return ResponseEntity.ok(new ApiResponse("장바구니 추가 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("장바구니 추가 실패"));

        }
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse> updateCart(@Valid @RequestBody CartUpdateDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            cartService.updateCartItemQuantity(dto, userDetails.getUser());
            return ResponseEntity.ok(new ApiResponse("장바구니 수량 업데이트 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("장바구니 수량 업데이트 실패"));
        }
    }
}
