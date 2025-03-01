package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.ProductCatDTO;
import com.CeyBazaar.backend.dto.ProductDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.entity.Product;
import com.CeyBazaar.backend.entity.ProductCat;
import com.CeyBazaar.backend.repository.ProductCatRepository;
import com.CeyBazaar.backend.repository.ProductRepository;
import com.CeyBazaar.backend.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductCatRepository productCatRepository;

    private final ProductRepository productRepository;

    private static final Logger logger  = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductCatRepository productCatRepository, ProductRepository productRepository) {
        this.productCatRepository = productCatRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Response<String> addNewProductCat(ProductCatDTO productCatDTO) {
        try {
            ProductCat productCat = new ProductCat();
            productCat.setCategoryName(productCatDTO.getCategoryName());
            productCat.setAddedBy(productCat.getAddedBy());
            productCat.setAddedOn(LocalDateTime.now());

            productCatRepository.save(productCat);
            logger.info("New Product Category Added ");
            return new Response<>(Constants.SUCCESS , "Product Category Added", "Successfully added " + productCatDTO.getCategoryName());

        }catch (Exception ex){
            return new Response<>(Constants.RUNTIME_EXCEPTION , "Failed to add new category" , ex.getMessage() );
        }
    }

    @Override
    public Response<String> addNewProduct(ProductDTO productDTO , MultipartFile imageFile) {
        try{
            Optional<ProductCat> productCat = productCatRepository.findById(productDTO.getProductCat().getId());
            if(productCat.isPresent()){
                Product product = new Product();
                product.setProductName(productDTO.getProductName());
                product.setPrice(productDTO.getPrice());
                product.setQuantity(productDTO.getQuantity());
                product.setWeight(productDTO.getWeight());
                product.setDescription(productDTO.getDescription());
                product.setAddedBy(productDTO.getAddedBy());
                product.setAddedOn(LocalDate.now());
                product.setProductCat(productDTO.getProductCat());

                if (imageFile != null && !imageFile.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = Paths.get(Constants.UPLOAD_DIRECTORY, fileName);
                    Files.createDirectories(imagePath.getParent());
                    Files.write(imagePath, imageFile.getBytes());
                    product.setImagePath(imagePath.toString());

                    productRepository.save(product);
                    logger.info("New Product added successfully " + productDTO.getProductName());
                    return new Response<>(Constants.SUCCESS , "New Product added successfully " , "New Product added successfully " + productDTO.getProductName());
                } else {
                    logger.error("Image is required for added new product");
                    return new Response<>(Constants.NOT_FOUND , "Image Not found" , "Image is required for add new product");
                }
            } else {
                logger.error("Product category not found");
                return new Response<>(Constants.NOT_FOUND , "Category not found" , null);
            }

        }catch (Exception ex){
            logger.error("Error while adding new product " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION , "Error while adding new product" , ex.getMessage());
        }
    }

    @Override
    public Response<List<ProductDTO>> viewProductList() {
        try{
            List<ProductDTO> productDTOS = new ArrayList<>();
            List<Product> products = productRepository.findAll();

            for(Product product : products){
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(product.getId());
                productDTO.setProductName(product.getProductName());
                productDTO.setPrice(product.getPrice());
                productDTO.setQuantity(product.getQuantity());
                productDTO.setWeight(product.getWeight());
                productDTO.setDescription(product.getDescription());
                productDTO.setAddedBy(product.getAddedBy());
                productDTO.setAddedOn(product.getAddedOn());
                productDTO.setUpdateBy(product.getUpdateBy());
                productDTO.setUpdatedOn(product.getUpdatedOn());
                productDTO.setTerminatedBy(product.getTerminatedBy());
                productDTO.setProductCatName(product.getProductCat().getCategoryName());
                productDTO.setImagePath(product.getImagePath());

                productDTOS.add(productDTO);
            }

            logger.info( "Product list retrieve successfully" );
            return new Response<>(Constants.SUCCESS ,"Product list retrieve successfully" , productDTOS);
        } catch (Exception ex){
            logger.error("Error while retrieving product list | " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION ,"Error while retrieving product list", null);
        }
    }

    @Override
    public Response<ProductDTO> viewProductById(int id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                ProductDTO productDTO = new ProductDTO();

                productDTO.setId(product.getId());
                productDTO.setProductName(product.getProductName());
                productDTO.setPrice(product.getPrice());
                productDTO.setQuantity(product.getQuantity());
                productDTO.setWeight(product.getWeight());
                productDTO.setDescription(product.getDescription());
                productDTO.setAddedBy(product.getAddedBy());
                productDTO.setAddedOn(product.getAddedOn());
                productDTO.setUpdateBy(product.getUpdateBy());
                productDTO.setUpdatedOn(product.getUpdatedOn());
                productDTO.setTerminatedBy(product.getTerminatedBy());
                productDTO.setProductCatName(product.getProductCat().getCategoryName());
                productDTO.setImagePath(product.getImagePath());

                logger.info("Product retrieved successfully for ID: " + id);
                return new Response<>(Constants.SUCCESS, "Product retrieved successfully", productDTO);
            } else {
                logger.warn("Product not found for ID: " + id);
                return new Response<>(Constants.NOT_FOUND, "Product not found for the given ID", null);
            }
        } catch (Exception ex) {
            logger.error("Error while retrieving product by ID | " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION, "Error while retrieving product", null);
        }
    }

    @Override
    public Response<List<ProductCatDTO>> viewProductCatList() {
        try{
            List<ProductCatDTO> productCatDTOS = new ArrayList<>();
            List<ProductCat> productCats = productCatRepository.findAll();

            for(ProductCat productCat : productCats){
                ProductCatDTO productCatDTO = new ProductCatDTO();
                productCatDTO.setId(productCat.getId());
                productCatDTO.setCategoryName(productCat.getCategoryName());

                productCatDTOS.add(productCatDTO);
            }

            logger.info( "Product category list retrieve successfully" );
            return new Response<>(Constants.SUCCESS ,"Product category list retrieve successfully" , productCatDTOS);
        } catch(Exception ex) {
            logger.error("Error while retrieving product category list | " + ex.getMessage());
            return new Response<>(Constants.RUNTIME_EXCEPTION ,"Error while retrieving product category list", null);
        }
    }
}
