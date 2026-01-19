package com.boot_demo1.ecommerce_springboot.Controller;


import com.boot_demo1.ecommerce_springboot.Config.AppConstant;
import com.boot_demo1.ecommerce_springboot.Payload.CategoryDto;
import com.boot_demo1.ecommerce_springboot.Payload.CategoryResponse;
import com.boot_demo1.ecommerce_springboot.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
   private CategoryService categoryService;

//    @GetMapping("/public/categories")
    @RequestMapping(value = "/public/categories" , method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse>getAllCategories(
            @RequestParam (name = "PageNumber" , defaultValue = AppConstant.PAGE_NUMBER ,required = false) Integer PageNumber
            ,@RequestParam (name = "PageSize" , defaultValue = AppConstant.PAGE_SIZE,required = false) Integer PageSize
,  @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        CategoryResponse categoryResponse = categoryService. getAllCategories(PageNumber,PageSize ,sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }


//    @PostMapping("/public/categories")
    @RequestMapping(value = "/public/categories" , method = RequestMethod.POST)
    public ResponseEntity<CategoryDto> CreateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new  ResponseEntity<>(categoryService.createcategory(categoryDto), HttpStatus.CREATED);
    }


//    @DeleteMapping("/admin/categories/{categoryId}")
    @RequestMapping(value = "/admin/categories/{categoryId}" , method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDto> DeleteCategory(@PathVariable Long categoryId) {
            CategoryDto deletecategory = categoryService.deletecategory(categoryId);
            return new ResponseEntity<>(deletecategory, HttpStatus.OK);

    }



//    @PutMapping("/admin/categories/{categoryId}")
    @RequestMapping(value = "/admin/categories/{categoryId}" , method = RequestMethod.PUT)
    public ResponseEntity<CategoryDto> UpdateCategory(@PathVariable Long categoryId,
                                                 @RequestBody CategoryDto categoryDto) {
       CategoryDto savedcategoryDto= categoryService.updatecategory(categoryId, categoryDto);
        return new ResponseEntity<>(savedcategoryDto, HttpStatus.OK);
    }



}
