package com.boot_demo1.ecommerce_springboot.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long  categoryId;
private String categoryName;
}
