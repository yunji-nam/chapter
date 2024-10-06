package com.example.chapter.dto;

import com.example.chapter.entity.Category;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookRegistrationDto {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;
    private Category category;
    @PastOrPresent
    private LocalDate publishedDate;
    private int price;
    private String description;
    private int quantity;

}
