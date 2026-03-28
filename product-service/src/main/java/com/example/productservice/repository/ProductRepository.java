package com.example.productservice.repository;

import com.example.productservice.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findByCategory(String category);
    List<ProductEntity> findByActiveTrue();
    Optional<ProductEntity> findBySku(String sku);

    Page<ProductEntity> findByNameContainingIgnoreCaseAndCategory(String name, String category, Pageable pageable);
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<ProductEntity> findByCategory(String category, Pageable pageable);
    Page<ProductEntity> findByActiveTrue(Pageable pageable);
}
