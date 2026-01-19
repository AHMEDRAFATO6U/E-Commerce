package com.boot_demo1.ecommerce_springboot.Repo;

import com.boot_demo1.ecommerce_springboot.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo  extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);


    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cart_Id=?2")
    Cart findByEmailAndCart_Id(String emailId, Long cartId);
}
