package com.boot_demo1.ecommerce_springboot.Repo;


import com.boot_demo1.ecommerce_springboot.Model.Category;
import com.boot_demo1.ecommerce_springboot.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByProductPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String productName);
}
