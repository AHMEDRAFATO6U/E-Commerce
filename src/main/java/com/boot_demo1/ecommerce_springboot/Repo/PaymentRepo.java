package com.boot_demo1.ecommerce.Repo;


import com.boot_demo1.ecommerce.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{

}