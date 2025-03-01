package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.ProductCatDTO;
import com.CeyBazaar.backend.dto.ProductDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/Products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/NewProductCat")
    public ResponseEntity<Response<String>> addNewProductCat(@RequestBody ProductCatDTO productCatDTO){
        return ResponseEntity.ok(productService.addNewProductCat(productCatDTO));
    }

    @PostMapping("/NewProduct")
    public ResponseEntity<Response<String>> addNewProduct(@ModelAttribute ProductDTO productDTO ,
                                                          @RequestParam MultipartFile imageFile){
        return ResponseEntity.ok(productService.addNewProduct(productDTO , imageFile));
    }

    @GetMapping("/ViewProductList")
    public ResponseEntity<Response<List<ProductDTO>>> viewProductList(){
        return ResponseEntity.ok(productService.viewProductList());
    }

    @GetMapping("/Product/{id}")
    public ResponseEntity<Response<ProductDTO>> getProductById(@PathVariable int id){
        return ResponseEntity.ok(productService.viewProductById(id));
    }

    @GetMapping("/ViewProductCatList")
    public ResponseEntity<Response<List<ProductCatDTO>>> viewProductCatList(){
        return ResponseEntity.ok(productService.viewProductCatList());
    }
}
