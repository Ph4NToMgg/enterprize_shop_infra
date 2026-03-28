package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.ReservationRequest;
import com.example.inventoryservice.dto.ReservationResponse;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<?> getInventory(@RequestParam(required = false) String sku) {
        if (sku != null) {
            return ResponseEntity.ok(inventoryService.getBySku(sku));
        }
        return ResponseEntity.ok(inventoryService.getAll());
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = inventoryService.reserve(request);
        return ResponseEntity.ok(response);
    }
}
