package com.champsoft.cardshop.BusinessLogicLayer;

import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;
import com.champsoft.cardshop.DataAccessLayer.Repositories.CollectorRepository;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorRequest;
import com.champsoft.cardshop.PresentationLayer.DTO.Collector.CollectorResponse;
import com.champsoft.cardshop.PresentationLayer.Mapper.CollectorMapper;
import com.champsoft.cardshop.Utilities.DuplicateResourceException;
import com.champsoft.cardshop.Utilities.CollectorOwnsCardsException;
import com.champsoft.cardshop.Utilities.CollectorNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class CollectorService {

    private final CollectorRepository collectorRepository;

    // Dependency Injection
    public CollectorService(CollectorRepository repository) {
        this.collectorRepository = repository;
    }

    // CRUD operation
    @Transactional(readOnly = true)
    public List<CollectorResponse> getAll() {
        return collectorRepository.findAll()
                .stream().map(CollectorMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CollectorResponse getById(Long id) {
        Collector collector = collectorRepository.findById(id)
                .orElseThrow(() -> new CollectorNotFoundException(id));
        return CollectorMapper.toResponse(collector);
    }

    @Transactional
    public CollectorResponse create(CollectorRequest req) {
        if (collectorRepository.existsByEmailIgnoreCase(req.email())) {
            throw new DuplicateResourceException("Collector email already exists: " + req.email());
        }
        Collector entity = CollectorMapper.toEntity(req);
        Collector saved = collectorRepository.save(entity);
        return CollectorMapper.toResponse(saved);
    }

    @Transactional
    public CollectorResponse update(Long id, CollectorRequest req) {
        Collector current = collectorRepository.findById(id)
                .orElseThrow(() -> new CollectorNotFoundException(id));
        boolean emailChanging = req.email() != null
                && !req.email().equalsIgnoreCase(current.getEmail());
        if (emailChanging && collectorRepository.existsByEmailIgnoreCase(req.email())) {
            throw new DuplicateResourceException("Collector email already exists: " + req.email());
        }
        current.setFirstName(req.firstName());
        current.setLastName(req.lastName());
        current.setEmail(req.email());
        current.setPhone(req.phone());
        Collector saved = collectorRepository.save(current);
        return CollectorMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Collector owner = collectorRepository.findById(id)
                .orElseThrow(() -> new CollectorNotFoundException(id));
        if (owner.getCards() != null && !owner.getCards().isEmpty()) {
            throw new CollectorOwnsCardsException(id);
        }
        collectorRepository.delete(owner);
    }

    // Flexible Search
    @Transactional(readOnly = true)
    public List<CollectorResponse> search(
            String firstName,
            String lastName,
            String emailContains,
            String addressContains,
            String phoneContains,
            Instant minCreated,
            Instant maxCreated,
            Instant minUpdated,
            Instant maxUpdated
    ) {
        String firstNorm = normalize(firstName);
        String lastNorm = normalize(lastName);
        String emailNorm = normalize(emailContains);
        String addressNorm = normalize(addressContains);
        String phoneNorm = normalize(phoneContains);
        return collectorRepository.searchAll(
                        firstNorm,
                        lastNorm,
                        emailNorm,
                        addressNorm,
                        phoneNorm,
                        minCreated,
                        maxCreated,
                        minUpdated,
                        maxUpdated
                )
                .stream().map(CollectorMapper::toResponse).toList();
    }

    // To call Collector in other services
    @Transactional(readOnly = true)
    public Collector getEntityById(Long id) {
        return collectorRepository.findById(id)
                .orElseThrow(() -> new CollectorNotFoundException(id));
    }

    // Helper Methods
    private static String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
