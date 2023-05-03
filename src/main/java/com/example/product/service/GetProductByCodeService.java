package com.example.product.service;

import com.example.product.model.entity.Product;
import com.example.product.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductByCodeService {
    @Autowired
    private ProductsRepository productsRepository;

    public Product execute(String code) {

        return productsRepository.findById(code).get();

    }
}
