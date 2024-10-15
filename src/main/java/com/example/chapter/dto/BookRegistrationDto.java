package com.example.chapter.dto;

import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookRegistrationDto {

    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String publisher;
    @NotEmpty
    private String isbn;
    @Min(1)
    private int pages;
    @NotNull
    private Category category;
    @PastOrPresent
    private LocalDate publishedDate;
    @Min(1)
    private int price;
    @NotEmpty
    private String description;
    @Min(1)
    private int quantity;

    public BookRegistrationDto(BookResponseDto dto) {
        this.title = dto.getTitle();
        this.author = dto.getAuthor();
        this.publisher = dto.getPublisher();
        this.isbn = dto.getIsbn();
        this.pages = dto.getPages();
        this.category = dto.getCategory();
        this.publishedDate = dto.getPublishedDate();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.quantity = dto.getStockQuantity();
    }

    public Book toEntity() {
        return new Book(title, author, publisher, isbn, pages, category, publishedDate, price, description, quantity);
    }

}
