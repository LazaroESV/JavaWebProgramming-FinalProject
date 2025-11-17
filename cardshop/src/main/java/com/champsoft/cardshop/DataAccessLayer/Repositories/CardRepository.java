package com.champsoft.cardshop.DataAccessLayer.Repositories;

import com.champsoft.cardshop.DataAccessLayer.Entities.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    // Derived queries
    List<Card> findByCollectorId(Long collectorId);
    boolean existsBySerialNumber(String serialNumber);

    // JPQL Queries
    @Query("""
            SELECT cd
            FROM Card cd
            WHERE (:pokemon IS NULL OR LOWER(cd.pokemon) LIKE LOWER(CONCAT('%', :pokemon, '%')))
            AND (:collectorId IS NULL OR cd.collector.id = :collectorId)
            AND (:grade IS NULL OR cd.grade = :grade)
            AND (:forSale IS NULL OR cd.forSale = :forSale)
            AND (:minPrice IS NULL OR cd.price >= :minPrice)
            AND (:maxPrice IS NULL OR cd.price <= :maxPrice)
            AND (:minYear IS NULL OR cd.releaseYear >= :minYear)
            AND (:maxYear IS NULL OR cd.releaseYear <= :maxYear)
            AND (:serialPart IS NULL OR LOWER(cd.serialNumber) LIKE LOWER(CONCAT('%', :serialPart, '%')))
            """)
    List<Card> searchAll(
            @Param("pokemon") String pokemon,
            @Param("collectorId") Long collectorId,
            @Param("grade") Double grade,
            @Param("forSale") Boolean forSale,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("serialPart") String serialPart
    );

}
