package com.example.servicetemplate.repository;

import com.example.servicetemplate.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, UUID> {
}
