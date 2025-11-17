package com.champsoft.cardshop.PresentationLayer.Controllers;

import com.champsoft.cardshop.BusinessLogicLayer.CardService;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cars")
@Validated
public class CardController {

    private final CardService cardService;

    // Dependency Injection
    public CardController(CardService service) {
        this.cardService = service;
    }

    // HTTP methods
    @GetMapping
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<CardResponse> responseBody = cardService.getAll();
        if (responseBody.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id) {
        CardResponse responseBody = cardService.getById(id);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest requestBody) {
        CardResponse responseBody = cardService.create(requestBody);
        return ResponseEntity.created(URI.create("/cards/" + responseBody.id()))
                .body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long id,
                                                 @Valid @RequestBody CardRequest requestBody) {
        CardResponse responseBody = cardService.update(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.ok("Card " + id + " deleted.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CardResponse>> searchCards(
            @RequestParam(required = false) String pokemon,
            @RequestParam(required = false) Long collectorId,
            @RequestParam(required = false) Double grade,
            @RequestParam(required = false) Boolean forSale,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) String serialContains
    ) {
        List<CardResponse> responseBody = cardService.search(
                pokemon,
                collectorId,
                grade,
                forSale,
                minPrice,
                maxPrice,
                minYear,
                maxYear,
                serialContains);

        if (responseBody.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseBody);
    }
}

