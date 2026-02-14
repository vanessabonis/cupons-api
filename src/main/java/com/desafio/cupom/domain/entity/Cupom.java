package com.desafio.cupom.domain.entity;

import com.desafio.cupom.domain.enums.CupomStatus;
import com.desafio.cupom.domain.value.Code;
import com.desafio.cupom.domain.value.DiscountValue;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cupom", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cupom_code", columnNames = "code")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "code", nullable = false, length = 6))
    private Code code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "discount_value", nullable = false, precision = 10, scale = 2))
    private DiscountValue discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(nullable = false)
    private Boolean redeemed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CupomStatus status = CupomStatus.ACTIVE;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Factory method para criar novo cupom
    public static Cupom create(String rawCode, String description, BigDecimal discountAmount,
                               LocalDateTime expirationDate, Boolean published) {

        Code code = Code.of(rawCode);
        DiscountValue discountValue = DiscountValue.of(discountAmount);

        validateExpirationDate(expirationDate);

        return Cupom.builder()
            .code(code)
            .description(description)
            .discountValue(discountValue)
            .expirationDate(expirationDate)
            .published(published != null && published)
            .redeemed(false)
            .status(CupomStatus.ACTIVE)
            .build();
    }

    // Validação de data de expiração
    private static void validateExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Data de expiração não pode ser nula");
        }

        LocalDateTime now = LocalDateTime.now();
        if (expirationDate.isBefore(now)) {
            throw new IllegalArgumentException(
                String.format("Data de expiração não pode estar no passado. Recebido: %s", expirationDate)
            );
        }
    }

    // Método de negócio: deletar cupom (soft delete)
    public void delete() {
        if (this.status == CupomStatus.DELETED) {
            throw new IllegalStateException(
                String.format("Cupom com ID %s já foi deletado", this.id)
            );
        }

        this.status = CupomStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    // Getters específicos para campos embedded
    public String getCodeValue() {
        return code != null ? code.getValue() : null;
    }

    public BigDecimal getDiscountValueAmount() {
        return discountValue != null ? discountValue.getValue() : null;
    }

    // Método auxiliar: verificar se está ativo
    public boolean isActive() {
        return this.status == CupomStatus.ACTIVE;
    }

    // Método auxiliar: verificar se está deletado
    public boolean isDeleted() {
        return this.status == CupomStatus.DELETED;
    }
}

