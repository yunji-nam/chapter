package com.example.chapter.service;

import com.example.chapter.dto.TrackingNumberDto;
import com.example.chapter.entity.Delivery;
import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.repository.DeliveryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // 운송장 등록
    @Transactional
    public void registerTrackingNumber(Long orderId, TrackingNumberDto dto) {
        Delivery delivery = findDelivery(orderId);

        String trackingNumber = dto.getTrackingNumber();
        delivery.addTrackingNumber(trackingNumber);

    }

    // 배송상태 업데이트
    public void updateDeliveryStatus(Long orderId, DeliveryStatus status) {
        Delivery delivery = findDelivery(orderId);
        delivery.updateDeliveryStatus(status);
    }

    private Delivery findDelivery(Long orderId) {
        Delivery delivery = deliveryRepository.findDeliveryByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("delivery를 찾을 수 없습니다."));
        return delivery;
    }
}