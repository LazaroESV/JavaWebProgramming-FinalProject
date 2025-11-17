package com.champsoft.cardshop.PresentationLayer.Mapper;

import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorResponse;

public final class CollectorMapper {
    private CollectorMapper() {
    }

    // Turn request into an entity
    public static Collector toEntity(CollectorRequest req) {
        return Collector.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .address(req.address())
                .phone(req.phone())
                .build();
    }

    // Turn entity into a response
    public static CollectorResponse toResponse(Collector collector) {
        long count = collector.getCards() == null ? 0 : collector.getCards().size();
        return new CollectorResponse(
                collector.getId(),
                collector.getFirstName(),
                collector.getLastName(),
                collector.getEmail(),
                collector.getAddress(),
                collector.getPhone(),
                collector.getCreatedAt(),
                collector.getUpdatedAt(),
                count
        );
    }

}
