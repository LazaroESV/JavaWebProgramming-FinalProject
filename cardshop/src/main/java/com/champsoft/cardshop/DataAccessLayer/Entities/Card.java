package com.champsoft.cardshop.DataAccessLayer.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(
        name = "cards_inventory",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_card_registration", columnNames = "serial_number")
        },
        indexes = {
                @Index(name = "idx_pokemon", columnList = "pokemon"),
                @Index(name = "idx_grade", columnList = "grade"),
                @Index(name = "idx_collector_id", columnList = "collector_id")
        }
)

@EntityListeners(AuditingEntityListener.class)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", nullable=false, length=40)
    private String serialNumber;

    @Column(name = "pokemon", nullable=false, length=60)
    private String pokemon;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(name = "grade", nullable = false)
    private Double grade;

    @Column(name = "for_sale", nullable=false)
    private boolean forSale;

    @Column(name = "release_year", nullable=false)
    private int releaseYear;

    @Column(name = "price", nullable=false)
    private int price;

    // Creates relationship to Collector database (One collector, many cards)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "collector_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_car_collector")
    )
    private Collector collector;

    @CreatedDate
    @Column(nullable=false, updatable=false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable=false)
    private Instant updatedAt;
}
