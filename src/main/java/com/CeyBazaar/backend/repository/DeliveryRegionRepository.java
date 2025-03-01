package com.CeyBazaar.backend.repository;

import com.CeyBazaar.backend.entity.DeliveryRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRegionRepository extends JpaRepository<DeliveryRegion , Integer> {
}
