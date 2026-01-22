package com.boot_demo1.ecommerce.ServiceImple;


import com.boot_demo1.ecommerce.Exceptions.APIException;
import com.boot_demo1.ecommerce.Exceptions.ResourceNotFoundException;
import com.boot_demo1.ecommerce.Model.*;
import com.boot_demo1.ecommerce.Model.Security.Address;
import com.boot_demo1.ecommerce.Payload.OrderDto;
import com.boot_demo1.ecommerce.Payload.OrderItemDto;
import com.boot_demo1.ecommerce.Repo.*;
import com.boot_demo1.ecommerce.Repo.Security.AddressRepo;
import com.boot_demo1.ecommerce.Service.CartService;
import com.boot_demo1.ecommerce.Service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepo productRepo;

    @Override
    @Transactional
    public OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepo.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotal_price());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepo.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepo.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setProductQuantity(product.getProductQuantity() - quantity);

            // Save product back to the database
            productRepo.save(product);

            // Remove items from cart
            cartService.deleteProductFromCart(cart.getCart_Id(), item.getProduct().getProductId());
        });

        OrderDto orderDTO = modelMapper.map(savedOrder, OrderDto.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDto.class)));

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }
}

