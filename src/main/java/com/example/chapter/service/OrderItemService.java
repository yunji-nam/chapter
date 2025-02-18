package com.example.chapter.service;

import com.example.chapter.dto.BookSalesDto;
import com.example.chapter.entity.Category;
import com.example.chapter.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public Page<BookSalesDto> getBookSales(Category category, String sortType, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        LocalDate now = LocalDate.now();

        if (startDate == null || startDate.isBefore(oneYearAgo)) {
            startDate = oneYearAgo;
        }
        if (endDate == null || endDate.isAfter(now)) {
            endDate = now;
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        if (category == null) {
            return orderItemRepository.getSales(startDateTime, endDateTime, sortType, pageable);
        } else {
            return orderItemRepository.getSalesByCategory(category, startDateTime, endDateTime, sortType, pageable);
        }

    }
}
