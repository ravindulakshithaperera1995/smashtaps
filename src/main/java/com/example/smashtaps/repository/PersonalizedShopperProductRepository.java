package com.example.smashtaps.repository;

import com.example.smashtaps.model.PersonalizedShopperProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonalizedShopperProductRepository extends JpaRepository<PersonalizedShopperProduct, String>,
        JpaSpecificationExecutor<PersonalizedShopperProduct> {

    @Query("SELECT a from PersonalizedShopperProduct a WHERE a.shopperId = :shopperId")
    List<PersonalizedShopperProduct> findAllByShopperId(final String shopperId);
}
