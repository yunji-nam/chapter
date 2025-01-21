package com.example.chapter.controller;

import com.example.chapter.dto.OrderListDto;
import com.example.chapter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderAdminController {

    private final OrderService orderService;

    @GetMapping("/order/search")
    public String search(@RequestParam String query,
                         @RequestParam String condition,
                         @PageableDefault(size = 5) Pageable pageable,
                         Model model) {
        Page<OrderListDto> searchResults = orderService.searchOrder(query, condition, pageable);

        model.addAttribute("orderList", searchResults);
        model.addAttribute("query", query);
        model.addAttribute("condition", condition);

        return "admin/order/search";
    }
}
