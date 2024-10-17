package com.example.chapter.service;

import com.example.chapter.dto.CartDto;
import com.example.chapter.dto.CartItemDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Cart;
import com.example.chapter.entity.CartItem;
import com.example.chapter.entity.User;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    public void addCart(CartDto dto, User user) {
        Book book = findBook(dto.getBookId());

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> new Cart(user));

        // 장바구니 내에 동일한 아이템 추가 시 수량만 업데이트
        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(book.getId())) {
                item.updateQuantity(item.getQuantity() + dto.getQuantity());
                cartRepository.save(cart);
                return;
            }
        }
        cart.addItem(book, dto.getQuantity());
        cartRepository.save(cart);
    }

    public List<CartItemDto> getCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<CartItem> items = cart.getItems();
        List<CartItemDto> itemDtos = items.stream().map(item ->
                new CartItemDto(item.getBook().getTitle(), item.getBook().getPrice(), item.getQuantity())).toList();
        return itemDtos;
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
    }

}
