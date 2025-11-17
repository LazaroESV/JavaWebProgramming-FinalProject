package com.champsoft.cardshop.PresentationLayer.DTO.Collector;

import java.time.Instant;

public record CollectorResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String address,
        String phone,
        Instant createdAt,
        Instant updatedAt,
        long cardCount
){}
