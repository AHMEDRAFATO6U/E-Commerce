package com.boot_demo1.ecommerce_springboot.Repo;

import com.boot_demo1.ecommerce_springboot.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    @Query(" SELECT ci FROM CartItem ci WHERE ci.cart.cart_Id = ?1 AND ci.product.productId= ?2 ")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Modifying // TELL THE DATABASE THAT U CAN APLLY CHANGES FROM JPA
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cart_Id = ?1 AND ci.product.productId = ?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
}
