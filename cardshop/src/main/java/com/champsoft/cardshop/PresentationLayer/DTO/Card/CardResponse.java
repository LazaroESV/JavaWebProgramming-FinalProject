package com.champsoft.cardshop.PresentationLayer.DTO.Card;

import java.time.Instant;

public record CardResponse(
        Long id,
        String serialNumber,
        String pokemon,
        double grade,
        boolean forSale,
        int releaseYear,
        int price,
        CollectorSummary collector
){}

