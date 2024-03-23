package com.example.smashtaps.controller.vo;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVO {
    private String productId;
    private String category;
    private String brand;
}
