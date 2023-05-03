package com.example.product;

import com.example.product.controller.ProductController;
import com.example.product.model.entity.Product;
import com.example.product.model.response.GetProductsResponse;
import com.example.product.repository.ProductsRepository;
import com.example.product.service.GetProductByCodeService;
import com.example.product.service.GetProductsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ProductApplicationTests {

    String baseUri = "/api/products";
    Product product = Product.builder()
            .code("123")
            .name("camera")
            .category("mobiles")
            .brand("samsung")
            .type("mobiles")
            .description("good camera")
            .build();
    @Mock
    private GetProductsService getProductsService;
    @Mock
    private GetProductByCodeService getProductByCodeService;
    @Mock
    private ProductsRepository productsRepository;
    @InjectMocks
    private ProductController productController;

    @Test
    void getProducts_should_200() {
        Pageable pageable = PageRequest.of(1, 10);
        when(getProductsService.execute(pageable)).thenReturn(GetProductsResponse.builder()
                .data(List.of(
                        product
                ))
                .build());

        ResponseEntity<Object> responseEntity = productController.getProducts("1", "10");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void getProductByCode_should_200() {
        Pageable pageable = PageRequest.of(1, 10);
        when(getProductByCodeService.execute(any())).thenReturn(product);

        ResponseEntity<Object> responseEntity = productController.getProductByCode("123");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void createProduct_should_201() {
        when(productsRepository.save(product)).thenReturn(product);

        ResponseEntity<Product> responseEntity = productController.createProduct(product);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void updateProduct_should_200() {
        when(productsRepository.findById(product.getCode())).thenReturn(Optional.of(product));
        when(productsRepository.save(product)).thenReturn(product);


        ResponseEntity<Product> responseEntity = productController.updateProduct(product.getCode(), product);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void deleteProduct_should_200() {

        ResponseEntity<HttpStatus> responseEntity = productController.deleteProduct(product.getCode());
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(204);
    }


}
