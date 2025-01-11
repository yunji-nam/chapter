package com.example.chapter.service;

import com.example.chapter.dto.CartDto;
import com.example.chapter.dto.CartItemDto;
import com.example.chapter.entity.*;
import com.example.chapter.exception.OutOfStockException;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.CartItemRepository;
import com.example.chapter.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.example.chapter.constant.CacheConstant.CART_COUNT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    // 장바구니 추가
    @CacheEvict(cacheNames = CART_COUNT, key= "#user.id")
    public void addCartItem(CartDto dto, User user) {
        Book book = findBook(dto.getBookId());

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> new Cart(user));

        boolean itemExists = false;
        // 장바구니 내에 동일한 아이템 추가 시 수량만 업데이트
        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(book.getId())) {
                item.updateQuantity(item.getQuantity() + dto.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            cart.addItem(book, dto.getQuantity());
        }
        cartRepository.save(cart);
    }

    // 장바구니 조회
    public List<CartItemDto> getCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> new Cart(user));
        List<CartItem> items = cart.getItems().stream().sorted(Comparator.comparing(CartItem::getId).reversed()).toList();

        return items.stream().map(CartItemDto::new).toList();
    }

    // 아이템 수량 수정
    @Transactional
    @CacheEvict(cacheNames = CART_COUNT, key= "#user.id")
    public void updateCartItemQuantity(CartDto dto, User user) {
        CartItem cartItem = cartItemRepository.findById(dto.getCartItemId())
                .orElseThrow(() -> new EntityNotFoundException("cart item을 찾을 수 없습니다."));
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("사용자가 다릅니다.");
        }
        cartItem.updateQuantity(dto.getQuantity());
    }

    // 전체 삭제
    @Transactional
    @CacheEvict(cacheNames = CART_COUNT, key= "#user.id")
    public void deleteCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new EntityNotFoundException("cart을 찾을 수 없습니다."));
        cartRepository.delete(cart);
    }

    // 장바구니 내 아이템 삭제
    @Transactional
    @CacheEvict(cacheNames = CART_COUNT, key= "#user.id")
    public void deleteItemFromCart(Long id, User user) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("cart item을 찾을 수 없습니다."));
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("사용자가 다릅니다.");
        }
        Cart cart = cartItem.getCart();
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    // 장바구니 아이템 수량 계산
    @Cacheable(cacheNames = CART_COUNT, key= "#user.id")
    public int getCartItemQuantity(User user) {
        return cartRepository.getCartItemQuantityByUserId(user.getId());
    }

    private Book findBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
        if (book.getStockQuantity() <= 0) {
            book.updateStatus(BookStatus.OUT_OF_STOCK);
            throw new OutOfStockException("재고가 부족합니다.");
        }
        return book;
    }

}
