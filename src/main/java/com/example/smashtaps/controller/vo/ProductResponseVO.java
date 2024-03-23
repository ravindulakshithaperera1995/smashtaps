package com.example.smashtaps.controller.vo;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseVO {
    private String shopperId;
    private String productId;
    private String category;
    private String brand;
    private BigDecimal relevancyScore;
}
