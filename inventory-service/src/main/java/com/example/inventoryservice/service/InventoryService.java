package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.dto.ReservationRequest;
import com.example.inventoryservice.dto.ReservationResponse;
import java.util.List;

public interface InventoryService {
    InventoryDto getBySku(String sku);
    List<InventoryDto> getAll();
    ReservationResponse reserve(ReservationRequest request);
    void releaseStock(String sku, int quantity);
    void addStock(String sku, int quantity);
}
