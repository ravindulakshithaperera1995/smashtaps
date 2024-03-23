package com.example.smashtaps.repository;

import com.example.smashtaps.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT a from Product a WHERE a.productId = :productId")
    Optional<Product> findByProductId(@Param("productId") final String productId);
}
