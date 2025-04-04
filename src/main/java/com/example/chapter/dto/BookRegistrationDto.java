package com.example.chapter.dto;

import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;
    @Min(1)
    private int price;
    @NotEmpty
    private String description;
    @Min(0)
    private int quantity;
    private MultipartFile image;
    private String imageUrl;

    public BookRegistrationDto(BookDetailDto dto) {
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
        this.imageUrl = dto.getImage();
    }

    public Book toEntity(String imageUrl) {
        return new Book(title, author, publisher, isbn, pages, category, publishedDate, price, description, quantity, imageUrl);
    }
}
