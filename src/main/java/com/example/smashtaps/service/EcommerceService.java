package com.example.smashtaps.service;

import com.example.smashtaps.controller.vo.PersonalizedShopperProductRequestVO;
import com.example.smashtaps.controller.vo.ProductFilterVO;
import com.example.smashtaps.controller.vo.ProductResponseVO;
import com.example.smashtaps.controller.vo.ProductVO;
import com.example.smashtaps.model.PersonalizedShopperProduct;
import com.example.smashtaps.model.Product;
import com.example.smashtaps.repository.PersonalizedShopperProductRepository;
import com.example.smashtaps.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class EcommerceService {

    private ProductRepository productRepository;
    private PersonalizedShopperProductRepository personalizedShopperProductRepository;

    @Autowired
    public EcommerceService(final ProductRepository productRepository,
                            final PersonalizedShopperProductRepository personalizedShopperProductRepository){

        this.productRepository = productRepository;
        this.personalizedShopperProductRepository = personalizedShopperProductRepository;
    }

    public void saveProduct(final ProductVO productVO){
        String id = UUID.randomUUID().toString();
        Product product = Product
                .builder()
                .id(id)
                .productId(productVO.getProductId())
                .brand(productVO.getBrand())
                .category(productVO.getCategory())
                .build();

        productRepository.save(product);
    }

    public void saveProductList(final PersonalizedShopperProductRequestVO vo){
        List<PersonalizedShopperProduct> products = new ArrayList<>();

        vo.getProducts().forEach(prod -> {
            String id = UUID.randomUUID().toString();

            Optional<Product> optional = productRepository.findByProductId(prod.getProductId());

            if(optional.isPresent()){
                PersonalizedShopperProduct personalizedShopperProduct = PersonalizedShopperProduct
                        .builder()
                        .id(id)
                        .shopperId(vo.getShopperId())
                        .product(optional.get())
                        .relevancyScore(prod.getRelevancyScore())
                        .build();

                products.add(personalizedShopperProduct);
            }
        });

        personalizedShopperProductRepository.saveAll(products);
    }

    public Page<ProductResponseVO> getProductsPerUser(final ProductFilterVO productFilterVO,
                                              int pageNo, int pageSize,
                                              Set<String> sortBy, String sortDirection) throws BadRequestException {

        if(pageSize < 10 || pageSize > 100){
            throw new BadRequestException("Invalid page size");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Specification<PersonalizedShopperProduct> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<PersonalizedShopperProduct, Product> join = root.join
                    ("product", JoinType.INNER);

            sortBy.forEach(s -> {
                if(s.equals("shopperId")){
                    if("desc".equalsIgnoreCase(sortDirection)){
                        query.orderBy(criteriaBuilder.desc(root.get(s)));
                    } else {
                        query.orderBy(criteriaBuilder.asc(root.get(s)));
                    }
                } else if(s.equals("category") || s.equals("brand")){
                    if("desc".equalsIgnoreCase(sortDirection)){
                        query.orderBy(criteriaBuilder.desc(join.get(s)));
                    } else {
                        query.orderBy(criteriaBuilder.asc(join.get(s)));
                    }
                }
            });

            if(StringUtils.hasText(productFilterVO.getShopperId())){
                predicates.add(criteriaBuilder.equal(root.get("shopperId") ,productFilterVO.getShopperId()));
            }

            if(StringUtils.hasText(productFilterVO.getCategory())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(join.get("category")), criteriaBuilder.lower
                        (criteriaBuilder.literal("%" + productFilterVO.getCategory() + "%"))));
            }

            if(StringUtils.hasText(productFilterVO.getBrand())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(join.get("brand")), criteriaBuilder.lower
                        (criteriaBuilder.literal("%" + productFilterVO.getBrand() + "%"))));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<PersonalizedShopperProduct> shopperProductPage = personalizedShopperProductRepository
                .findAll(specification, pageable);

        List<ProductResponseVO> productResponse = shopperProductPage
                .get().map(this::buildProductResponse)
                .toList();

        return new PageImpl<>(productResponse, pageable, shopperProductPage.getTotalElements());
    }

    private ProductResponseVO buildProductResponse(final PersonalizedShopperProduct product){
        return ProductResponseVO.builder()
                .shopperId(product.getShopperId())
                .productId(product.getProduct().getProductId())
                .relevancyScore(product.getRelevancyScore())
                .brand(product.getProduct().getBrand())
                .category(product.getProduct().getCategory())
                .build();
    }
}
