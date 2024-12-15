package com.example.chapter.controller;

import com.example.chapter.dto.BookListDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/user/book/likes")
    public String getLikes(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<BookListDto> likeList = likeService.getLikeList(userDetails.getUser());
        model.addAttribute("likeList", likeList);
        return "user/likeList";
    }
}
