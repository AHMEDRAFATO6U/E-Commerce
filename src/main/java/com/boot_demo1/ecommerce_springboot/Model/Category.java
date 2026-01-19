package com.boot_demo1.ecommerce_springboot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = "Ecom_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long  categoryId;
    @NotBlank
    @Size(min = 2, max = 50,message = "THE NAME MUST HAVE AT LEAST TWO CHAR AND AT MAXIMUM 50 CHAR  ")
    private String categoryName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> productList;


}
