package com.example.chapter.repository;

import com.example.chapter.dto.BookSalesDto;
import com.example.chapter.entity.Category;
import com.example.chapter.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select new com.example.chapter.dto.BookSalesDto(oi.book.id, oi.book.category, oi.book.title, oi.book.author, oi.book.price, sum(oi.quantity), sum(oi.quantity * oi.book.price))" +
            " from OrderItem oi join oi.order o" +
            " where o.status = 'PAID' and" +
            " o.createdAt between :startedAt and :endedAt" +
            " group by oi.book.id" +
            " order by " +
            " case when :sortType = 'quantity' then sum(oi.quantity) end desc," +
            " case when :sortType = 'sales' then sum(oi.quantity * oi.book.price) end desc")
    Page<BookSalesDto> getSales(LocalDateTime startedAt, LocalDateTime endedAt, String sortType, Pageable pageable);

    @Query("select new com.example.chapter.dto.BookSalesDto(oi.book.id, oi.book.category, oi.book.title, oi.book.author, oi.book.price, sum(oi.quantity), sum(oi.quantity * oi.book.price))" +
            " from OrderItem oi join oi.order o" +
            " where o.status = 'PAID' and" +
            " o.createdAt between :startedAt and :endedAt and" +
            " oi.book.category = :category" +
            " group by oi.book.id" +
            " order by " +
            " case when :sortType = 'quantity' then sum(oi.quantity) end desc," +
            " case when :sortType = 'sales' then sum(oi.quantity * oi.book.price) end desc")
    Page<BookSalesDto> getSalesByCategory(Category category, LocalDateTime startedAt, LocalDateTime endedAt, String sortType, Pageable pageable);
}
