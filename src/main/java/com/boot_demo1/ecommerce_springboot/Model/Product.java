package com.boot_demo1.ecommerce_springboot.Model;

import com.boot_demo1.ecommerce_springboot.Model.Security.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Ecom_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String image;
    @NotNull(message = "Product price is required")
    private double productPrice;
    private String productDescription;
    private double specialPrice;
    private int productQuantity;
    private  double discount;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Seller_id")
    private User user;



    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "product" , cascade
            ={CascadeType.PERSIST , CascadeType.MERGE
            , CascadeType.REFRESH} , fetch = FetchType.EAGER )
    private List<CartItem> cartItem = new ArrayList<CartItem>();


}
