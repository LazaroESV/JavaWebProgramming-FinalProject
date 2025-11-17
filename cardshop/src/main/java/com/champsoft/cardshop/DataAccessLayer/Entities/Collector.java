package com.champsoft.cardshop.DataAccessLayer.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "collectors",
        uniqueConstraints = { @UniqueConstraint(name = "uk_collector_email", columnNames = "email") }
)
@EntityListeners(AuditingEntityListener.class)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Collector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=80)
    private String firstName;

    @Column(nullable=false, length=80)
    private String lastName;

    @Column(nullable=false, length=255)
    private String email;

    @Column(nullable=false, length=255)
    private String address;

    @Column(length=40)
    private String phone;

    @CreatedDate
    @Column(nullable=false, updatable=false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable=false)
    private Instant updatedAt;

    // Creates relationship to Card database (One collector, many cards)
    @OneToMany(mappedBy = "collector", fetch = FetchType.LAZY)

    @Builder.Default
    private List<Card> cards = new ArrayList<>();
}
