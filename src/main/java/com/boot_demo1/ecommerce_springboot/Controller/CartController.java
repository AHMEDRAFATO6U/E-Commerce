package com.boot_demo1.ecommerce_springboot.Controller;

import com.boot_demo1.ecommerce_springboot.Model.Cart;
import com.boot_demo1.ecommerce_springboot.Payload.CartDto;
import com.boot_demo1.ecommerce_springboot.Repo.CartRepo;
import com.boot_demo1.ecommerce_springboot.Service.CartService;

import com.boot_demo1.ecommerce_springboot.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    AuthUtil authUtil;
    @Autowired
    CartRepo cartRepo;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart (@PathVariable Long productId,
                                                     @PathVariable Integer quantity) {
         CartDto cartDto= cartService.addproduct(productId,quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getCarts(){
        List<CartDto> cartDto =cartService.getAllCarts();
        return new  ResponseEntity<List<CartDto>>(cartDto, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDto> getUserCart(){
        String emailId=authUtil.loggedInEmail();
        Cart cart=cartRepo.findCartByEmail(emailId);
        Long cartId = cart.getCart_Id();
        CartDto cartDto =cartService.getCartUser(emailId,cartId);
        return new ResponseEntity<>(cartDto, HttpStatus.FOUND);

    }


    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDto> UpdateCartProduct(@PathVariable Long productId
            , @PathVariable String operation){
        CartDto cartDto = cartService.updateProductQuantityInCart(productId
         , operation.equalsIgnoreCase("delete")? -1:1);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }


    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> DeleteCartProduct(@PathVariable Long productId,
                                                    @PathVariable Long cartId){

        String status=cartService.deleteProductFromCart(productId,cartId);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }


}
