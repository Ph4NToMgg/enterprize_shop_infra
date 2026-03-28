package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductDto;
import com.example.productservice.entity.ProductEntity;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductDto create(ProductDto dto) {
        ProductEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAll() {
        return mapper.toDtoList(repository.findByActiveTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getByCategory(String category) {
        return mapper.toDtoList(repository.findByCategory(category));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> search(String name, String category, Pageable pageable) {
        if (name != null && category != null) {
            return repository.findByNameContainingIgnoreCaseAndCategory(name, category, pageable)
                    .map(mapper::toDto);
        } else if (name != null) {
            return repository.findByNameContainingIgnoreCase(name, pageable)
                    .map(mapper::toDto);
        } else if (category != null) {
            return repository.findByCategory(category, pageable)
                    .map(mapper::toDto);
        }
        return repository.findByActiveTrue(pageable).map(mapper::toDto);
    }

    @Override
    public ProductDto update(UUID id, ProductDto dto) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
