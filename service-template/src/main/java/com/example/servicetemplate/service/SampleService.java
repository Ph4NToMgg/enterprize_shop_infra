package com.example.servicetemplate.service;

import com.example.servicetemplate.dto.SampleDto;

import java.util.List;
import java.util.UUID;

public interface SampleService {

    SampleDto create(SampleDto dto);

    SampleDto getById(UUID id);

    List<SampleDto> getAll();

    SampleDto update(UUID id, SampleDto dto);

    void delete(UUID id);
}
