package com.boot_demo1.ecommerce_springboot.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long cartItem_Id;
    private CartDto cart;
    private ProductDto productDto;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
