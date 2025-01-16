package com.example.chapter.controller;

import com.example.chapter.dto.BookListDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/user/book/likes")
    public String getLikes(@AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable, Model model) {
        Page<BookListDto> likeList = likeService.getLikeList(userDetails.getUser(), pageable);
        if (likeList.isEmpty()) {
            return "like/empty";
        }
        model.addAttribute("likeList", likeList);
        return "like/list";
    }
}
