package com.example.chapter.entity;

import com.example.chapter.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int quantity;

    public CartItem(Cart cart, Book book, int quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        if (quantity > book.getStockQuantity()) {
            throw new OutOfStockException("재고가 부족합니다.");
        }
        this.quantity = quantity;
    }
}
