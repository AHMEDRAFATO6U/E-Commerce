package com.boot_demo1.ecommerce_springboot.ServiceImple;

import com.boot_demo1.ecommerce_springboot.Exceptions.APIException;
import com.boot_demo1.ecommerce_springboot.Exceptions.ResourceNotFoundException;
import com.boot_demo1.ecommerce_springboot.Model.Category;
import com.boot_demo1.ecommerce_springboot.Payload.CategoryDto;
import com.boot_demo1.ecommerce_springboot.Payload.CategoryResponse;
import com.boot_demo1.ecommerce_springboot.Repo.CategoryRepo;
import com.boot_demo1.ecommerce_springboot.Service.CategoryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class CategoryServiceImple implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public CategoryResponse getAllCategories(Integer PageNumber , Integer PageSize , String sortBy , String sortOrder) throws APIException {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(PageNumber, PageSize ,sortByAndOrder);
        Page page = categoryRepo.findAll(pageable);
        List<Category> categories = page.getContent();
        if (categories.isEmpty()) {
            throw  new APIException("No Category Found");
        }
        List<CategoryDto> categoryDtos = categories.stream()
                .map(category -> modelMapper.map(category ,CategoryDto.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(page.getNumber());
        categoryResponse.setPageSize(page.getSize());
        categoryResponse.setTotalPages(page.getTotalPages());
        categoryResponse.setTotalElements(page.getTotalElements());
        categoryResponse.setTotalPages(page.getTotalPages());
        categoryResponse.setIsFirstPage(page.isFirst());
        categoryResponse.setIsLastPage(page.isLast());
        return categoryResponse;
    }

    @Override
    @Transactional
    public CategoryDto createcategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);

        Category categoryFromDb = categoryRepo.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new APIException("Category already exists with  "+category.getCategoryName());
        }
        Category savedCategory = categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto deletecategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Categoey","id",categoryId));
        categoryRepo.delete(category);
    return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto updatecategory(Long categoryId, CategoryDto categoryDto) {
        Category savedCategory = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Categoey","id",categoryId));

        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCategoryId(categoryId);
        categoryRepo.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }
}
