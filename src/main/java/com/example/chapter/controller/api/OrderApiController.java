package com.example.chapter.controller.api;

import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.api.ApiResponse;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.OrderService;
import com.example.chapter.service.PaymentService;
import com.example.chapter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderApiController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;

    // 주문 취소
    @DeleteMapping("/api/order/{orderId}")
    public ApiResponse<String> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.cancelOrder(orderId, userDetails.getUser());
        return new ApiResponse<>("주문 취소 성공");
    }

    @PostMapping("/api/test-order")
    public ApiResponse<String> completeOrder(@RequestBody OrderRequestDto dto) {
        User testUser = userService.getUser(1L);
        String testMerchantUid = "TEST12345";
        paymentService.completePayment(testMerchantUid, testUser, dto);
        return new ApiResponse<>("테스트 주문 완료");
    }
}
