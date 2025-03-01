package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.DeliveryRegionDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.entity.DeliveryRegion;
import com.CeyBazaar.backend.repository.DeliveryRegionRepository;
import com.CeyBazaar.backend.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryRegionServiceImpl implements DeliveryRegionService{

    @Autowired
    private DeliveryRegionRepository deliveryRegionRepository;

    private static final Logger logger = LoggerFactory.getLogger(DeliveryRegionServiceImpl.class);
    @Override
    public Response<String> addNewDeliveryRegion(DeliveryRegionDTO deliveryRegionDTO) {
        try{
            DeliveryRegion deliveryRegion = new DeliveryRegion();
            deliveryRegion.setRegionCode(deliveryRegionDTO.getRegionCode());
            deliveryRegion.setRegionName(deliveryRegionDTO.getRegionName());
            deliveryRegion.setShippingCost(deliveryRegionDTO.getShippingCost());
            deliveryRegion.setAddedBy("System Admin");
            deliveryRegion.setAddedOn(LocalDate.now());

            deliveryRegionRepository.save(deliveryRegion);
            logger.info("Successfully added new delivery region");
            return new Response<>(Constants.SUCCESS, "Successfully added new delivery region" , "Successfully added new delivery region"+ deliveryRegionDTO.getRegionName());
        } catch (Exception ex){
            logger.error("Error while adding delivery region " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION , "Error while adding delivery region" , null);
        }
    }

    @Override
    public Response<List<DeliveryRegionDTO>> viewDeliveryRegion() {
        try{
            List<DeliveryRegionDTO> deliveryRegionDTOS = new ArrayList<>();
            List<DeliveryRegion> deliveryRegions = deliveryRegionRepository.findAll();

            for (DeliveryRegion deliveryRegion : deliveryRegions){
                DeliveryRegionDTO deliveryRegionDTO = new DeliveryRegionDTO();
                deliveryRegionDTO.setId(deliveryRegion.getId());
                deliveryRegionDTO.setRegionCode(deliveryRegion.getRegionCode());
                deliveryRegionDTO.setRegionName(deliveryRegion.getRegionName());
                deliveryRegionDTO.setShippingCost(deliveryRegion.getShippingCost());

                deliveryRegionDTOS.add(deliveryRegionDTO);
            }

            logger.info("Successfully retrieved delivery region list");
            return new Response<>(Constants.SUCCESS , "Successfully retrieved delivery region list" , deliveryRegionDTOS);
        } catch (Exception ex){
            logger.error("Error while retrieving delivery region list" + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION , "Error while retrieving delivery region list" , null);
        }
    }
}
