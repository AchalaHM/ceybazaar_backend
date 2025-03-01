package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.ProductCatDTO;
import com.CeyBazaar.backend.dto.ProductDTO;
import com.CeyBazaar.backend.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Response<String> addNewProductCat(ProductCatDTO productCatDTO);
    Response<String> addNewProduct(ProductDTO productDTO , MultipartFile imageFile);
    Response<List<ProductDTO>> viewProductList();
    Response<ProductDTO> viewProductById(int id);
    Response<List<ProductCatDTO>> viewProductCatList();
}
