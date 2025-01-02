package com.example.chapter.dto;

import com.example.chapter.entity.BookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookStatusUpdateDto {
    private List<Long> bookIds;
    private BookStatus status;
}
