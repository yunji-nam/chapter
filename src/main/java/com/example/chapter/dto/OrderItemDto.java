package com.example.chapter.dto;

import com.example.chapter.entity.OrderItem;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private int bookPrice;
    private String bookImage;
    @Min(1)
    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.bookId = orderItem.getBook().getId();
        this.bookTitle = orderItem.getBook().getTitle();
        this.bookPrice = orderItem.getBook().getPrice();
        this.bookImage = orderItem.getBook().getImage();
        this.quantity = orderItem.getQuantity();
    }
}
