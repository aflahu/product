package com.example.product.controller;

import com.example.product.model.entity.Product;
import com.example.product.repository.ProductsRepository;
import com.example.product.service.GetProductByCodeService;
import com.example.product.service.GetProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductsService getProductsService;
    private final GetProductByCodeService getProductByCodeService;
    private final ProductsRepository productsRepository;

    @GetMapping("")
    public ResponseEntity getProducts(
            @RequestParam(required = false, defaultValue = "1") String page,
            @RequestParam(required = false, defaultValue = "10") String size
    ) {
        try {
            Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
            return new ResponseEntity(getProductsService.execute(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{code}")
    public ResponseEntity getProductByCode(@PathVariable("code") String code) {
        try {
            Product product = getProductByCodeService.execute(code);
            if (product == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product _product = productsRepository
                    .save(Product.builder()
                            .code(product.getCode())
                            .name(product.getName())
                            .brand(product.getBrand())
                            .category(product.getCategory())
                            .type(product.getType())
                            .build());
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<Product> updateProduct(@PathVariable("code") String code, @RequestBody Product product) {
        Optional<Product> productData = productsRepository.findById(code);

        if (productData.isPresent()) {
            Product _product = Product.builder()
                    .code(productData.get().getCode())
                    .name(product.getName())
                    .category(product.getCategory())
                    .brand(product.getBrand())
                    .type(product.getType())
                    .description(product.getDescription())
                    .build();
            return new ResponseEntity<>(productsRepository.save(_product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("code") String code) {
        try {
            productsRepository.deleteById(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
