package com.boot_demo1.ecommerce_springboot.ServiceImple;
import com.boot_demo1.ecommerce_springboot.Exceptions.APIException;
import com.boot_demo1.ecommerce_springboot.Exceptions.ResourceNotFoundException;
import com.boot_demo1.ecommerce_springboot.Model.Category;
import com.boot_demo1.ecommerce_springboot.Model.Product;
import com.boot_demo1.ecommerce_springboot.Payload.ProductDto;
import com.boot_demo1.ecommerce_springboot.Payload.ProductResponse;
import com.boot_demo1.ecommerce_springboot.Repo.CategoryRepo;
import com.boot_demo1.ecommerce_springboot.Repo.ProductRepo;
import com.boot_demo1.ecommerce_springboot.Service.FileService;
import com.boot_demo1.ecommerce_springboot.Service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@Service
public class ProductserviceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image")
    String path="image/";


    @Override
    @Transactional
    public ProductDto addproduct(ProductDto productDto, Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        if (categoryId == null) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        } else {
            Product product = modelMapper.map(productDto, Product.class);
            product.setCategory(category);
            product.setImage("Defult.png");
            Double specialprice = product.getProductPrice() -
                    ((product.getDiscount() * 0.01) * product.getProductPrice());
            product.setProductPrice(specialprice);
            Product savedProduct = productRepo.save(product);
            return modelMapper.map(savedProduct, ProductDto.class);
        }
    }



    @Override
    @Transactional
    public ProductResponse getAllproducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = productRepo.findAll();
        if (products.isEmpty()) {
            throw new RuntimeException("No products found");
        } else {
            List<ProductDto> productDtos = products.stream().map(product -> modelMapper
                            .map(product, ProductDto.class))
                    .toList();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDtos);
            productResponse.setPageNumber(pageProducts.getNumber());
            productResponse.setPageSize(pageProducts.getSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            productResponse.setLastPage(pageProducts.isLast());
            return productResponse;
        }
    }

    @Override
    @Transactional
    public ProductResponse searchBycategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category =categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = productRepo.findByCategoryOrderByProductPriceAsc(category);
        List<ProductDto> productDtos = products.stream().map(product -> modelMapper
                        .map(product, ProductDto.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;

    }

    @Override
    @Transactional
    public ProductResponse searchBykeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepo.findAll(pageDetails);

        List<Product> products = productRepo.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        if (products.isEmpty()) {
            throw new APIException("No products found");
        } else {
            List<ProductDto> productDtos = products.stream().map(product -> modelMapper
                            .map(product, ProductDto.class))
                    .toList();
            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDtos);
            productResponse.setPageNumber(pageProducts.getNumber());
            productResponse.setPageSize(pageProducts.getSize());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            productResponse.setLastPage(pageProducts.isLast());

            return productResponse;
        }
    }

    @Override
    @Transactional
    public ProductDto updaateproduct(ProductDto productDto, Long productId) {

        Product savedproduct=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        savedproduct.setProductName(productDto.getProductName());
        savedproduct.setProductPrice(productDto.getProductPrice());
        savedproduct.setDiscount(productDto.getDiscount());
        savedproduct.setProductQuantity(productDto.getProductQuantity());
        savedproduct.setProductDescription(productDto.getProductDescription());

        productRepo.save(savedproduct);
        return modelMapper.map(savedproduct, ProductDto.class);
    }

    @Override
    public ProductDto deteteproduct(Long productId) {
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepo.delete(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateproductImage(Long productId, MultipartFile image) throws IOException {
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        String filename=fileService.uploadimage(path,image);
        product.setImage(filename);
        productRepo.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

}
