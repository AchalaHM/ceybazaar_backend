package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.ProductCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCatRepository extends JpaRepository<ProductCat , Integer> {
}
