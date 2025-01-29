package com.example.chapter.security;

import com.example.chapter.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    @ModelAttribute("cartItemQuantity")
    public Integer getCartItemQuantity(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return cartService.getCartItemQuantity(userDetails.getUser());
        }
        return 0;
    }

}
