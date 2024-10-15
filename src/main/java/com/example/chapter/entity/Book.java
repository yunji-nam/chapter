package com.example.chapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String publisher;

    @NotEmpty
    @Column(unique = true)
    private String isbn;

    @Min(1)
    private int pages;

    @NotNull
    private Category category;

    @NotNull
    private LocalDate publishedDate;

    @Min(1)
    private int price;

    @NotEmpty
    private String description;

    private int stockQuantity;

    private String image;

    public Book(String title, String author, String publisher, String isbn, int pages, Category category,
                LocalDate publishedDate, int price, String description, int quantity) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.pages = pages;
        this.category = category;
        this.publishedDate = publishedDate;
        this.price = price;
        this.description = description;
        this.stockQuantity = quantity;
    }

    public void update(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.isbn = book.getIsbn();
        this.pages = book.getPages();
        this.category = book.getCategory();
        this.publishedDate = book.getPublishedDate();
        this.price = book.getPrice();
        this.description = book.getDescription();
        this.stockQuantity = book.getStockQuantity();
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void decreaseStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }

}