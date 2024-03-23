package com.example.smashtaps.controller.vo;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestVO {
    private String productId;
    private BigDecimal relevancyScore;
}
