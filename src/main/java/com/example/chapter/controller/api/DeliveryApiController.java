package com.example.chapter.controller.api;

import com.example.chapter.dto.api.ApiResponse;
import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryApiController {

    private final DeliveryService deliveryService;

    @PutMapping("/api/admin/delivery/{orderId}/status")
    public ApiResponse<String> updateDeliveryStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatus(orderId, status);
        return new ApiResponse<>("배송정보 업데이트 완료");
    }

}
