package com.champsoft.cardshop.PresentationLayer.DTO.Card;

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

