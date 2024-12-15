package com.example.chapter.controller;

import com.example.chapter.dto.TrackingNumberDto;
import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/delivery")
public class DeliveryAdminController {

    private final DeliveryService deliveryService;

    @PostMapping("/{orderId}/tracking")
    public String registerTrackingNumber(@PathVariable Long orderId, TrackingNumberDto dto) {
        deliveryService.registerTrackingNumber(orderId, dto);
        return "redirect:/admin/delivery/list";
    }

    @PutMapping("/{orderId}/status")
    public String updateDeliveryStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatus(orderId, status);
        return "redirect:/admin/orders";
    }

}