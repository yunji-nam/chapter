package com.example.chapter.controller;

import com.example.chapter.dto.OrderListDto;
import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderAdminController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getOrders(@RequestParam(required = false) LocalDate startDate,
                            @RequestParam(required = false) LocalDate endDate,
                            @RequestParam(defaultValue = "0") int pageNo,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<OrderListDto> orders = orderService.getOrders(startDate, endDate, pageNo, size);

        model.addAttribute("orders", orders);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());

        return "admin/order/list";
    }

    @GetMapping("/order/search")
    public String search(@RequestParam(required = false) LocalDate startDate,
                         @RequestParam(required = false) LocalDate endDate,
                         @RequestParam String query,
                         @RequestParam String condition,
                         @RequestParam(defaultValue = "0") int pageNo,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        Page<OrderListDto> searchResults = orderService.searchOrder(startDate, endDate, query, condition, pageNo, size);

        model.addAttribute("orderList", searchResults);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("query", query);
        model.addAttribute("condition", condition);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());

        return "admin/order/search";
    }
}
