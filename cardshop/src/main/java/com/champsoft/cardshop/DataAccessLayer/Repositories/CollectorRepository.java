package com.champsoft.cardshop.DataAccessLayer.Repositories;

import com.champsoft.cardshop.DataAccessLayer.Entities.Collector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CollectorRepository extends JpaRepository<Collector, Long>{

    // Derived queries
    boolean existsByEmailIgnoreCase(String email);

    // JPQL Queries
    @Query("""
            SELECT c
            FROM Collector c
            WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
            AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
            AND (:emailPart IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :emailPart, '%')))
            AND (:addressPart IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :addressPart, '%')))
            AND (:phonePart IS NULL OR (c.phone IS NOT NULL AND LOWER(c.phone) LIKE LOWER(CONCAT('%', :phonePart, '%'))))
            AND (:minCreated IS NULL OR c.createdAt >= :minCreated)
            AND (:maxCreated IS NULL OR c.createdAt <= :maxCreated)
            AND (:minUpdated IS NULL OR c.updatedAt >= :minUpdated)
            AND (:maxUpdated IS NULL OR c.updatedAt <= :maxUpdated)
            """)
    List<Collector> searchAll(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("emailPart") String emailPart,
            @Param("addressPart") String addressPart,
            @Param("phonePart") String phonePart,
            @Param("minCreated") Instant minCreated,
            @Param("maxCreated") Instant maxCreated,
            @Param("minUpdated") Instant minUpdated,
            @Param("maxUpdated") Instant maxUpdated
    );

}
