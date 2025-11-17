package com.champsoft.cardshop.PresentationLayer.Mapper;

import com.champsoft.cardshop.DataAccessLayer.Entities.Card;
import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardResponse;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CollectorSummary;

public final class CardMapper {
    private CardMapper() {
    }

    // Turn request into an entity
    public static Card toEntity(CardRequest req, Collector collector) {
        return Card.builder()
                .pokemon(req.pokemon())
                .grade(req.grade())
                .forSale(req.forSale())
                .serialNumber(req.serialNumber())
                .releaseYear(req.releaseYear())
                .price(req.price())
                .collector(collector)
                .build();
    }

    // Turn entity into a response
    public static CardResponse toResponse(Card card) {
        Collector c = card.getCollector();
        CollectorSummary collectorDto = (c == null) ? null : new CollectorSummary(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getAddress(),
                c.getPhone()
        );
        return new CardResponse(
                card.getId(),
                card.getSerialNumber(),
                card.getPokemon(),
                card.getGrade(),
                card.isForSale(),
                card.getReleaseYear(),
                card.getPrice(),
                collectorDto
        );
    }

}
