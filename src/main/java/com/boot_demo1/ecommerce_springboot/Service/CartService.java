package com.boot_demo1.ecommerce_springboot.Service;

import com.boot_demo1.ecommerce_springboot.Payload.CartDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    CartDto addproduct(Long productId, Integer quantity);


    List<CartDto> getAllCarts();

    CartDto getCartUser(String emailId, Long cartId);

    @Transactional
    CartDto updateProductQuantityInCart(Long productId, Integer quantity);

    @Transactional
    String deleteProductFromCart(Long productId, Long cartId);
}
