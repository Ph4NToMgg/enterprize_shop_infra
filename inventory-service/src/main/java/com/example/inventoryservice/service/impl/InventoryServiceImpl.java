package com.example.inventoryservice.service.impl;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.dto.ReservationRequest;
import com.example.inventoryservice.dto.ReservationResponse;
import com.example.inventoryservice.entity.InventoryEntity;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getBySku(String sku) {
        InventoryEntity inv = repository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + sku));
        return InventoryDto.builder()
                .sku(inv.getSku())
                .available(inv.getAvailableQuantity() - inv.getReservedQuantity())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getAll() {
        return repository.findAll().stream()
                .map(inv -> InventoryDto.builder()
                        .sku(inv.getSku())
                        .available(inv.getAvailableQuantity() - inv.getReservedQuantity())
                        .build())
                .toList();
    }

    @Override
    public ReservationResponse reserve(ReservationRequest request) {
        log.info("Reserve request: sku={}, qty={}, correlationId={}",
                request.getSku(), request.getQuantity(), request.getCorrelationId());

        InventoryEntity inv = repository.findBySku(request.getSku())
                .orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + request.getSku()));

        int available = inv.getAvailableQuantity() - inv.getReservedQuantity();
        if (available < request.getQuantity()) {
            return ReservationResponse.builder()
                    .sku(request.getSku())
                    .reserved(false)
                    .correlationId(request.getCorrelationId())
                    .message("Insufficient stock. Available: " + available)
                    .build();
        }

        inv.setReservedQuantity(inv.getReservedQuantity() + request.getQuantity());
        repository.save(inv);

        return ReservationResponse.builder()
                .sku(request.getSku())
                .reserved(true)
                .correlationId(request.getCorrelationId())
                .message("Reserved " + request.getQuantity() + " units")
                .build();
    }

    @Override
    public void releaseStock(String sku, int quantity) {
        InventoryEntity inv = repository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Inventory not found for SKU: " + sku));
        inv.setReservedQuantity(Math.max(0, inv.getReservedQuantity() - quantity));
        repository.save(inv);
    }
}
