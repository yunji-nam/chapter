package com.example.chapter.controller;

import com.example.chapter.dto.BookSalesDto;
import com.example.chapter.entity.Category;
import com.example.chapter.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderItemAdminController {

    private final OrderItemService orderItemService;

    @GetMapping("/sales")
    public String getSales(@RequestParam(defaultValue = "sales") String sortType,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) LocalDate startDate,
                           @RequestParam(required = false) LocalDate endDate,
                           Pageable pageable,
                           Model model) {
        Category categoryValue = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryValue = Category.valueOf(category);
            } catch (IllegalArgumentException e) {
                categoryValue = null;
            }
        }
        Page<BookSalesDto> bookSales = orderItemService.getBookSales(categoryValue, sortType, startDate, endDate, pageable);

        model.addAttribute("sortType", sortType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("bookSales", bookSales);
        model.addAttribute("categories", Category.values());
        model.addAttribute("requestedCategory", category);

        return "admin/sales/list";
    }

}
