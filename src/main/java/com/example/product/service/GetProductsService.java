package com.example.product.service;

import com.example.product.model.response.GetProductsResponse;
import com.example.product.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    public GetProductsResponse execute(Pageable pageable) {

        if (pageable.getPageSize() > 25) {
          pageable  = PageRequest.of(pageable.getPageNumber(), 25);
        }

        return GetProductsResponse.builder()
                .data(productsRepository.findAll(pageable).getContent())
                .build();
    }
}
