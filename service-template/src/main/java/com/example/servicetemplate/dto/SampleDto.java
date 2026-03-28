package com.example.servicetemplate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleDto {

    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
