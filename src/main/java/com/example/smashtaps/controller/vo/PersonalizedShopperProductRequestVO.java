package com.example.smashtaps.controller.vo;

import lombok.*;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalizedShopperProductRequestVO {
    private String shopperId;
    private List<ProductRequestVO> products;
}
