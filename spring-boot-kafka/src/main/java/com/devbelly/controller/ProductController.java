package com.devbelly.controller;

import com.devbelly.entity.Product;
import com.devbelly.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PutMapping("{productId}")
    public void update(@PathVariable("productId") Long productId){
        productService.update(productId);
    }
}
