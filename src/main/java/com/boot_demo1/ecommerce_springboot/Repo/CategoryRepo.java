package com.boot_demo1.ecommerce_springboot.Repo;

import com.boot_demo1.ecommerce_springboot.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findByCategoryName(String categoryName);
}
