package com.example.chapter.controller;

import com.example.chapter.dto.OrderListDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderAdminController {

    private final OrderService orderService;

    // 주문 목록
    @GetMapping("/orders")
    public String getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               @RequestParam(required = false) LocalDateTime startDate,
                               @RequestParam(required = false) LocalDateTime endDate,
                               @RequestParam(defaultValue = "0") int pageNo,
                               @RequestParam(defaultValue = "2") int size,
                               Model model) {
        Page<OrderListDto> orders = orderService.getOrders(userDetails.getUser(), startDate, endDate, pageNo, size);

        model.addAttribute("orders", orders);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/order/list";
    }

}
