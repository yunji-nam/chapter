package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    // 주문 취소
    @DeleteMapping("/api/order/{orderId}")
    public ApiResponse<String> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderId, userDetails.getUser());
        return new ApiResponse<>("주문 취소 성공");
    }
}
