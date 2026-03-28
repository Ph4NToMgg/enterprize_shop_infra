package com.example.servicetemplate.service.impl;

import com.example.servicetemplate.dto.SampleDto;
import com.example.servicetemplate.entity.SampleEntity;
import com.example.servicetemplate.exception.ResourceNotFoundException;
import com.example.servicetemplate.mapper.SampleMapper;
import com.example.servicetemplate.repository.SampleRepository;
import com.example.servicetemplate.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SampleServiceImpl implements SampleService {

    private final SampleRepository repository;
    private final SampleMapper mapper;

    @Override
    public SampleDto create(SampleDto dto) {
        SampleEntity entity = mapper.toEntity(dto);
        SampleEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SampleDto getById(UUID id) {
        SampleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample", "id", id));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SampleDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public SampleDto update(UUID id, SampleDto dto) {
        SampleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample", "id", id));
        mapper.updateEntityFromDto(dto, entity);
        SampleEntity updated = repository.save(entity);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sample", "id", id);
        }
        repository.deleteById(id);
    }
}
