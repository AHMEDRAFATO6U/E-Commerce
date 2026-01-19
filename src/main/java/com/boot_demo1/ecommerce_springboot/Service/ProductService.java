package com.boot_demo1.ecommerce_springboot.Service;


import com.boot_demo1.ecommerce_springboot.Payload.ProductDto;
import com.boot_demo1.ecommerce_springboot.Payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto addproduct(ProductDto productDto, Long categoryId);


    ProductResponse searchBycategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchBykeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDto updaateproduct(ProductDto productDto, Long productId);

    ProductDto deteteproduct(Long productId);

    ProductDto updateproductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getAllproducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
