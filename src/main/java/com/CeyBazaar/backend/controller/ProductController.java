package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.ProductCatDTO;
import com.CeyBazaar.backend.dto.ProductDTO;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    } //done fe

    @PostMapping("/NewProduct")
    public ResponseEntity<Response<String>> addNewProduct(@RequestPart ProductDTO productDTO ,
                                                          @RequestParam MultipartFile imageFile ,
                                                          @RequestParam List<MultipartFile> additionalImages){
        return ResponseEntity.ok(productService.addNewProduct(productDTO , imageFile , additionalImages));
    } // done frontend

    @GetMapping("/ViewProductList")
    public ResponseEntity<Response<List<ProductDTO>>> viewProductList(){
        return ResponseEntity.ok(productService.viewProductList());
    } // done fe

    @GetMapping("/ViewLatestProductList")
    public ResponseEntity<Response<List<ProductDTO>>> viewLatestProductList(){
        return ResponseEntity.ok(productService.viewLatestProductList());
    } // done fe

    @GetMapping("/Product/{id}")
    public ResponseEntity<Response<ProductDTO>> getProductById(@PathVariable int id){
        return ResponseEntity.ok(productService.viewProductById(id));
    }

    @GetMapping("/ViewProductCatList")
    public ResponseEntity<Response<List<ProductCatDTO>>> viewProductCatList(){
        return ResponseEntity.ok(productService.viewProductCatList());
    } //done fe

    @PostMapping("/GetSimilarProducts")
    public ResponseEntity<Response<List<ProductDTO>>> viewSimilarProductList(@RequestBody Map<String, Integer> request){
        int id = request.get("categoryId");
        int productId = request.get("productId");
        return ResponseEntity.ok(productService.viewSimilarProductList(id , productId));
    }  // done fe
}
