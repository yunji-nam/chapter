package com.example.chapter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int pages;
    private Category category;
    private LocalDate publishedDate;
    private int price;
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

    public void update(String title, String author, String publisher, String isbn, int pages, Category category,
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