package com.example.servicetemplate.service.impl;

import com.example.servicetemplate.dto.SampleDto;
import com.example.servicetemplate.entity.SampleEntity;
import com.example.servicetemplate.exception.ResourceNotFoundException;
import com.example.servicetemplate.mapper.SampleMapper;
import com.example.servicetemplate.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SampleServiceImplTest {

    @Mock
    private SampleRepository repository;

    @Mock
    private SampleMapper mapper;

    @InjectMocks
    private SampleServiceImpl service;

    private UUID sampleId;
    private SampleEntity entity;
    private SampleDto dto;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        entity = SampleEntity.builder().id(sampleId).name("Test").description("Desc").build();
        dto = SampleDto.builder().id(sampleId).name("Test").description("Desc").build();
    }

    @Test
    @DisplayName("create - should save and return DTO")
    void create_shouldSaveAndReturnDto() {
        when(mapper.toEntity(any(SampleDto.class))).thenReturn(entity);
        when(repository.save(any(SampleEntity.class))).thenReturn(entity);
        when(mapper.toDto(any(SampleEntity.class))).thenReturn(dto);

        SampleDto result = service.create(dto);

        assertThat(result.getName()).isEqualTo("Test");
        verify(repository).save(any(SampleEntity.class));
    }

    @Test
    @DisplayName("getById - should return DTO when found")
    void getById_shouldReturnDto() {
        when(repository.findById(sampleId)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        SampleDto result = service.getById(sampleId);

        assertThat(result.getId()).isEqualTo(sampleId);
    }

    @Test
    @DisplayName("getById - should throw when not found")
    void getById_shouldThrowWhenNotFound() {
        when(repository.findById(sampleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(sampleId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getAll - should return list of DTOs")
    void getAll_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDtoList(anyList())).thenReturn(List.of(dto));

        List<SampleDto> result = service.getAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("delete - should throw when not found")
    void delete_shouldThrowWhenNotFound() {
        when(repository.existsById(sampleId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(sampleId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete - should delete when found")
    void delete_shouldDeleteWhenFound() {
        when(repository.existsById(sampleId)).thenReturn(true);

        service.delete(sampleId);

        verify(repository).deleteById(sampleId);
    }
}
