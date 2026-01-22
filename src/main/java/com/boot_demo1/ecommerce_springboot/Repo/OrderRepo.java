package com.boot_demo1.ecommerce.Repo;


import com.boot_demo1.ecommerce.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

}