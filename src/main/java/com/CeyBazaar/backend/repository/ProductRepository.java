package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
