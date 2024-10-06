package com.example.chapter.dto;

import com.example.chapter.entity.Category;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookResponseDto {

    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;
    private Category category;
    private LocalDate publishedDate;
    private int price;
    private String description;
    private String image;

}
