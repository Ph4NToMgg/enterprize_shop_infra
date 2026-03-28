package com.example.productservice.service;

import com.example.productservice.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto create(ProductDto dto);
    ProductDto getById(UUID id);
    List<ProductDto> getAll();
    List<ProductDto> getByCategory(String category);
    Page<ProductDto> search(String name, String category, Pageable pageable);
    ProductDto update(UUID id, ProductDto dto);
    void delete(UUID id);
}
