package com.example.chapter.service;

import com.example.chapter.dto.TrackingNumberDto;
import com.example.chapter.entity.Delivery;
import com.example.chapter.repository.DeliveryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    //운송장 등록
    @Transactional
    public void registerTrackingNumber(Long orderId, TrackingNumberDto dto) {
        Delivery delivery = deliveryRepository.findDeliveryByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("delivery를 찾을 수 없습니다."));

        String trackingNumber = dto.getTrackingNumber();
        delivery.addTrackingNumber(trackingNumber);

    }
}