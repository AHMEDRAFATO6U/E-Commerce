package com.boot_demo1.ecommerce_springboot.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private List<CategoryDto> content;
    private Integer PageNumber;
    private Integer PageSize;
    private Long TotalElements;
    private Integer TotalPages;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
