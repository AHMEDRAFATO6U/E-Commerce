package com.boot_demo1.ecommerce_springboot.Controller;


import com.boot_demo1.ecommerce_springboot.Config.AppConstant;
import com.boot_demo1.ecommerce_springboot.Payload.ProductDto;
import com.boot_demo1.ecommerce_springboot.Payload.ProductResponse;
import com.boot_demo1.ecommerce_springboot.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.boot_demo1.ecommerce_springboot.Config.AppConstant.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto,
                                                 @PathVariable  Long categoryId) {
        ProductDto productDto1 = productService.addproduct(productDto,categoryId);
        return new  ResponseEntity<>(productDto,HttpStatus.CREATED);
    }

    @GetMapping("/api/public/product")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllproducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("api/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductBycategory(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder

            ,@PathVariable Long categoryId) {
        ProductResponse productResponse=productService.searchBycategory(categoryId,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("api/public/keyword/{keyword}/products")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder
            ,@PathVariable String keyword) {
        ProductResponse productResponse=productService.searchBykeyword(keyword ,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @PutMapping("api/admin/product/{productId}")
    public ResponseEntity<ProductDto> UpdateProduct(@RequestBody ProductDto productDto
    ,@PathVariable  Long productId) {

        ProductDto productDto1 =productService.updaateproduct(productDto,productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/product/{productId}")
    public ResponseEntity<ProductDto> DeleteProduct(@PathVariable Long productId) {
        ProductDto deletedProduct =productService.deteteproduct(productId);
        return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
    }

    @PutMapping("/api/product/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image  ) throws IOException {
        ProductDto updatedDto=productService.updateproductImage(productId,image);
        return new ResponseEntity<>(updatedDto,HttpStatus.OK);

    }





}
