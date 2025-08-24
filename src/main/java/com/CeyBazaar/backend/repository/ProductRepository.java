package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByProductCat_IdAndIdNot(int productCat_id, int id);
    List<Product> findAllByOrderByAddedOnDesc();

    @Query("SELECT p FROM Product p WHERE p.addedOn = (SELECT MAX(p2.addedOn) FROM Product p2)")
    List<Product> findAllByLatestAddedOnDate();

}
