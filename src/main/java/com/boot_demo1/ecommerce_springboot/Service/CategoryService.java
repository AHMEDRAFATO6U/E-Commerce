package com.boot_demo1.ecommerce_springboot.Service;


import com.boot_demo1.ecommerce_springboot.Payload.CategoryDto;
import com.boot_demo1.ecommerce_springboot.Payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer PageNumber , Integer PageSize , String sortBy , String sortOrder);
    CategoryDto createcategory(CategoryDto categoryDto);

    CategoryDto deletecategory(Long categoryId);
    CategoryDto updatecategory(Long categoryId, CategoryDto category);
}
