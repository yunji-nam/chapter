package com.example.chapter.dto;


import com.example.chapter.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookImageUpdateDto {

    private Long bookId;
    private String imageUrl;
    private String title;
    private String author;

    public BookImageUpdateDto(Book book) {
        this.bookId = book.getId();
        this.imageUrl = book.getImage();
        this.title = book.getTitle();
        this.author = book.getAuthor();
    }
}
