package com.champsoft.cardshop.PresentationLayer.Controllers;

import com.champsoft.cardshop.BusinessLogicLayer.CardService;
import com.champsoft.cardshop.BusinessLogicLayer.CollectorService;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardResponse;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/collectors")
@Validated
public class CollectorController {

    private final CollectorService collectorService;
    private final CardService cardService;

    // Dependency Injection
    public CollectorController(CollectorService collectorService, CardService cardService) {
        this.collectorService = collectorService;
        this.cardService = cardService;
    }

    // HTTP methods
    @GetMapping
    public ResponseEntity<List<CollectorResponse>> getAllCollectors() {
        List<CollectorResponse> responseBody = collectorService.getAll();
        if (responseBody.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectorResponse> getCollectorById(@PathVariable Long id) {
        CollectorResponse responseBody = collectorService.getById(id);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<CollectorResponse> createCollector(@Valid @RequestBody CollectorRequest requestBody) {
        CollectorResponse responseBody = collectorService.create(requestBody);
        return ResponseEntity.created(URI.create("/collectors/" + responseBody.id()))
                .body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectorResponse> updateCollector(@PathVariable Long id,
                                                     @Valid @RequestBody CollectorRequest requestBody) {
        CollectorResponse responseBody = collectorService.update(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCollector(@PathVariable Long id) {
        collectorService.delete(id);
        return ResponseEntity.ok("Collector " + id + " deleted.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CollectorResponse>> searchCollectors(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String emailContains,
            @RequestParam(required = false) String addressContains,
            @RequestParam(required = false) String phoneContains,
            @RequestParam(required = false) Instant minCreated,
            @RequestParam(required = false) Instant maxCreated,
            @RequestParam(required = false) Instant minUpdated,
            @RequestParam(required = false) Instant maxUpdated
    ) {
        List<CollectorResponse> responseBody = collectorService.search(
                firstName,
                lastName,
                emailContains,
                addressContains,
                phoneContains,
                minCreated,
                maxCreated,
                minUpdated,
                maxUpdated
        );
        if (responseBody.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<List<CardResponse>> getCardsByCollector(@PathVariable Long id) {
        List<CardResponse> responseBody = cardService.getCardsByCollector(id);
        if (responseBody.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responseBody);
    }
}
