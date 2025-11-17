package com.champsoft.cardshop.PresentationLayer.DTO.Card;

import jakarta.validation.constraints.*;

public record CardRequest(
        @NotBlank String serialNumber,
        @NotBlank String pokemon,
        @NotBlank Double grade,
        @NotBlank Boolean forSale,
        @NotNull @Min(1996) Integer releaseYear,
        @NotNull @Min(0) Integer price,
        @NotNull @Positive Long collectorId
){}
