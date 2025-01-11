package com.example.chapter.dto;

import com.example.chapter.entity.Book;
import com.example.chapter.entity.BookStatus;
import com.example.chapter.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookListDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Category category;
    private LocalDate publishedDate;
    private int price;
    private int stockQuantity;
    private String image;
    private BookStatus status;
    private boolean likeStatus;

    public BookListDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.isbn = book.getIsbn();
        this.category = book.getCategory();
        this.publishedDate = book.getPublishedDate();
        this.price = book.getPrice();
        this.stockQuantity = book.getStockQuantity();
        this.image = book.getImage();
        this.status = book.getStatus();
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }


}
