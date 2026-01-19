package com.boot_demo1.ecommerce_springboot.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    private String productName;
    private String productDescription;
    private String image ;
    private double productPrice;
    private double specialPrice;
    private int productQuantity;
    private  double discount;

}
