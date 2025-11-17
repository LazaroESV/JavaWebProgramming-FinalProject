package com.champsoft.cardshop.PresentationLayer.DTO.Collector;

import jakarta.validation.constraints.*;

public record CollectorRequest(
        @NotBlank @Size(max=80) String firstName,
        @NotBlank @Size(max=80) String lastName,
        @NotBlank @Email String email,
        @NotBlank String address,
        @Size(max=40) String phone
){}
