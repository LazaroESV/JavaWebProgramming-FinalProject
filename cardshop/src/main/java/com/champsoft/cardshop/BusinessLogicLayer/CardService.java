package com.champsoft.cardshop.BusinessLogicLayer;

import com.champsoft.cardshop.DataAccessLayer.Entities.Card;
import com.champsoft.cardshop.DataAccessLayer.Repositories.CardRepository;
import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Card.CardResponse;
import com.champsoft.cardshop.Utilities.CardNotFoundException;
import com.champsoft.cardshop.Utilities.DuplicateResourceException;
import com.champsoft.cardshop.Utilities.InvalidCardDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.champsoft.cardshop.PresentationLayer.Mapper.CardMapper;

import java.time.Year;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CollectorService collectorService;

    // Dependency Injection
    public CardService(CardRepository cards, CollectorService collectors) {
        this.cardRepository = cards;
        this.collectorService = collectors;
    }

    // CRUD operations
    @Transactional(readOnly = true)
    public List<CardResponse> getAll() {
        return cardRepository.findAll()
                .stream().map(CardMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CardResponse getById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return CardMapper.toResponse(card);

    }

    @Transactional
    public CardResponse create(CardRequest req) {
        int currentYear = Year.now().getValue();

        if (req.releaseYear() > currentYear) {
            throw new InvalidCardDataException(
                    "The release year (" + req.releaseYear() + ") cannot be greater than the current year (" + currentYear + ")."
            );
        }

        if (cardRepository.existsBySerialNumber(req.serialNumber())) {
            throw new DuplicateResourceException("Serial number " + req.serialNumber() + " already exists");
        }

        Collector collector = collectorService.getEntityById(req.collectorId());
        Card entity = CardMapper.toEntity(req, collector);
        Card saved = cardRepository.save(entity);

        return CardMapper.toResponse(saved);
    }

    @Transactional
    public CardResponse update(Long id, CardRequest req) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        int currentYear = Year.now().getValue();

        if (req.releaseYear() > currentYear) {
            throw new InvalidCardDataException(
                    "The release year (" + req.releaseYear() + ") cannot be greater than the current year (" + currentYear + ")."
            );
        }

        boolean serialNumberChanging = !card.getSerialNumber().equals(req.serialNumber());
        if (serialNumberChanging && cardRepository.existsBySerialNumber(req.serialNumber())) {
            throw new DuplicateResourceException("Serial number " + req.serialNumber() + " already exists");
        }

        Collector collector = collectorService.getEntityById(req.collectorId());
        card.setPokemon(req.pokemon());
        card.setGrade(req.grade());
        card.setForSale(req.forSale());
        card.setSerialNumber(req.serialNumber());
        card.setReleaseYear(req.releaseYear());
        card.setPrice(req.price());
        card.setCollector(collector);
        Card saved = cardRepository.save(card);
        return CardMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        cardRepository.delete(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponse> getCardsByCollector(Long collectorId) {
        // Validate if collector exists
        collectorService.getEntityById(collectorId);
        return cardRepository.findByCollectorId(collectorId)
                .stream()
                .map(CardMapper::toResponse)
                .toList();
    }

    // Flexible search
    @Transactional(readOnly = true)
    public List<CardResponse> search(
            String pokemon,
            Long collectorId,
            Double grade,
            Boolean forSale,
            Integer minPrice,
            Integer maxPrice,
            Integer minYear,
            Integer maxYear,
            String serialPart
    ) {
        String pokemonNorm = normalize(pokemon);
        String serialNorm = normalize(serialPart);
        return cardRepository.searchAll(
                pokemonNorm,
                collectorId,
                grade,
                forSale,
                minPrice,
                maxPrice,
                minYear,
                maxYear,
                serialNorm
        ).stream().map(CardMapper::toResponse).toList();
    }

    // Helper Methods
    private static String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
