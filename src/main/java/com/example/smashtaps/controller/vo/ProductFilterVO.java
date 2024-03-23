package com.example.smashtaps.controller.vo;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductFilterVO {
    private String shopperId;
    private String category;
    private String brand;
}
