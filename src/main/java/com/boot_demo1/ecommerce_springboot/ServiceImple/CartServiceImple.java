package com.boot_demo1.ecommerce_springboot.ServiceImple;

import com.boot_demo1.ecommerce_springboot.Exceptions.APIException;
import com.boot_demo1.ecommerce_springboot.Exceptions.ResourceNotFoundException;
import com.boot_demo1.ecommerce_springboot.Model.Cart;
import com.boot_demo1.ecommerce_springboot.Model.CartItem;
import com.boot_demo1.ecommerce_springboot.Model.Product;
import com.boot_demo1.ecommerce_springboot.Payload.CartDto;
import com.boot_demo1.ecommerce_springboot.Payload.ProductDto;
import com.boot_demo1.ecommerce_springboot.Repo.CartItemRepo;
import com.boot_demo1.ecommerce_springboot.Repo.CartRepo;
import com.boot_demo1.ecommerce_springboot.Repo.ProductRepo;
import com.boot_demo1.ecommerce_springboot.Service.CartService;
import com.boot_demo1.ecommerce_springboot.Util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImple implements CartService {
    @Autowired
    CartRepo cartRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    CartItemRepo cartItemRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDto addproduct(Long productId, Integer quantity) {

        Cart cart =createCart();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cart.getCart_Id(), productId);
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }
        if(quantity==0){
            throw new APIException("Product " + product.getProductName() + " has no quantity to add");
        }
        if(product.getProductQuantity()<quantity){
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getProductQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getProductPrice()*quantity);
        newCartItem.setProductPrice(product.getProductPrice());
        cartItemRepo.save(newCartItem);


        product.setProductQuantity(product.getProductQuantity());
        cart.setTotal_price(cart.getTotal_price()+(product.getProductPrice()*quantity));
        cartRepo.save(cart);


        CartDto cartDto = modelMapper.map(cart, CartDto.class);

        List<CartItem> cartItems=cart.getCartItems();

        Stream<ProductDto> productStream = cartItems.stream().map(item -> {
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setProductQuantity(item.getQuantity());
            return map;
        });

        cartDto.setProducts(productStream.toList());
        return cartDto;
    }

    @Override
    @Transactional
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        if(carts.isEmpty()){
            throw new APIException("No cart found");
        }

        List<CartDto> cartDTOs = carts.stream().map(cart -> {
            CartDto cartDTO = modelMapper.map(cart, CartDto.class);

            List<ProductDto> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    @Transactional
    public CartDto getCartUser(String emailId, Long cartId) {
        Cart cart=cartRepo.findByEmailAndCart_Id(emailId,cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDto cartDTO = modelMapper.map(cart, CartDto.class);

        cart.getCartItems().forEach(c ->
                c.getProduct().setProductQuantity(c.getQuantity()));

        List<ProductDto> products=cart.getCartItems().stream()
                .map(p->modelMapper.map(p.getProduct(),ProductDto.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDto updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId=authUtil.loggedInEmail();
        Cart userCart=cartRepo.findCartByEmail(emailId);
        Long CartId=userCart.getCart_Id();

        Cart cart =cartRepo.findById(CartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", CartId));


        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if(product.getProductQuantity()==0){
            throw new APIException("Product " + product.getProductName() + " has no quantity to add");
        }
        if(product.getProductQuantity()<quantity){
            throw new APIException("Product " + product.getProductName() + " has no quantity to add");
        }

        CartItem cartItem=cartItemRepo.findCartItemByProductIdAndCartId(cart.getCart_Id(),productId);
        if(cartItem==null){
            throw new ResourceNotFoundException("CartItem", "cartId", cart.getCart_Id());
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIException("The resulting quantity cannot be negative.");
        }else {
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getProductPrice());
            cartItem.setDiscount(product.getProductPrice() * quantity);

            cart.setTotal_price(cart.getTotal_price() + (product.getProductPrice() * quantity));
            cartRepo.save(cart);
        }

        CartItem updatedItem = cartItemRepo.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepo.deleteById(updatedItem.getCartItem_Id());
        }


        CartDto cartDTO = modelMapper.map(cart, CartDto.class);
        List<CartItem> cartItems=cart.getCartItems();

        Stream<ProductDto> productStream=cartItems.stream().map(item->{
            ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
            map.setProductQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long productId, Long cartId) {

        Cart cart =cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem =cartItemRepo.findCartItemByProductIdAndCartId(cart.getCart_Id(),productId);
        if(cartItem==null){
            throw new ResourceNotFoundException("CartItem", "cartId", cart.getCart_Id());
        }
        cart.setTotal_price(cart.getTotal_price()
                -cartItem.getProduct().getProductPrice());
        cart.setTotal_price(cart.getTotal_price()
                +cartItem.getProduct().getProductPrice());
        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "product"+cartItem.getProduct().getProductName()+" has been deleted";
    }


    private Cart createCart() {
        Cart userCart = cartRepo.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotal_price(0.00);
        cart.setUser(authUtil.loggedInUser());

        return cartRepo.save(cart);
    }


}
