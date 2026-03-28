package com.example.servicetemplate.controller;

import com.example.servicetemplate.dto.SampleDto;
import com.example.servicetemplate.service.SampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @PostMapping
    public ResponseEntity<SampleDto> create(@Valid @RequestBody SampleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sampleService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(sampleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SampleDto>> getAll() {
        return ResponseEntity.ok(sampleService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleDto> update(@PathVariable UUID id, @Valid @RequestBody SampleDto dto) {
        return ResponseEntity.ok(sampleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
