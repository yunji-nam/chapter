package com.example.chapter.dto;

import com.example.chapter.entity.Book;
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

    public BookResponseDto(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.isbn = book.getIsbn();
        this.pages = book.getPages();
        this.category = book.getCategory();
        this.publishedDate = book.getPublishedDate();
        this.price = book.getPrice();
        this.description = book.getDescription();
        this.image = book.getImage();
    }

}
