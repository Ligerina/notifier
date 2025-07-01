package ru.liger.defi.notifier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "triggers")
@Getter
@Setter
public class Trigger {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "position_name", nullable = false)
    private String positionName;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "upper_bound", nullable = false, precision = 20, scale = 8)
    private BigDecimal upperBound;

    @Column(name = "lower_bound", nullable = false, precision = 20, scale = 8)
    private BigDecimal lowerBound;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
