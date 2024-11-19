package com.example.chapter.entity;

import jakarta.persistence.*;
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

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(unique = true)
    private String isbn;

    private int pages;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate publishedDate;

    private int price;

    @Column(nullable = false)
    private String description;

    private int stockQuantity;

    private String image;

    public Book(String title, String author, String publisher, String isbn, int pages, Category category,
                LocalDate publishedDate, int price, String description, int quantity, String image) {
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
        this.image = image;
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