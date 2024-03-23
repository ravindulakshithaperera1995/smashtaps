package com.example.smashtaps.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "personalized_shopper_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedShopperProduct {
    @Id
    private String id;
    private String shopperId;
    private BigDecimal relevancyScore;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
