package com.example.smashtaps.controller;

import com.example.smashtaps.controller.vo.PersonalizedShopperProductRequestVO;
import com.example.smashtaps.controller.vo.ProductFilterVO;
import com.example.smashtaps.controller.vo.ProductResponseVO;
import com.example.smashtaps.controller.vo.ProductVO;
import com.example.smashtaps.service.EcommerceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/e-commerce")
public class EcommerceController {

    private EcommerceService ecommerceService;

    @Autowired
    public EcommerceController(final EcommerceService ecommerceService){
        this.ecommerceService = ecommerceService;
    }

    @PostMapping("/product")
    public void createProduct(@RequestBody final ProductVO productVO){
        ecommerceService.saveProduct(productVO);
    }

    @PostMapping("/shopper-data")
    public void createShopperData(@RequestBody final PersonalizedShopperProductRequestVO productVO){
        ecommerceService.saveProductList(productVO);
    }

    @GetMapping("/shopper-data")
    public Page<ProductResponseVO> getShopperData(@RequestParam(required = false) final String shopperId,
                                                  @RequestParam(required = false) final String brand,
                                                  @RequestParam(required = false) final String category,
                                                  @RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "") Set<String> sortBy,
                                                  @RequestParam(required = false, defaultValue = "desc") String sortDirection) throws BadRequestException {

        return ecommerceService.getProductsPerUser(ProductFilterVO.builder()
                        .shopperId(shopperId)
                        .category(category)
                        .brand(brand)
                        .build(), pageNumber,
                pageSize, sortBy, sortDirection);
    }
}
