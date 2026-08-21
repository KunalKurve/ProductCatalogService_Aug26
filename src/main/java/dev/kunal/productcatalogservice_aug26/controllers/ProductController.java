package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.dtos.CategoryDto;
import dev.kunal.productcatalogservice_aug26.dtos.ProductDto;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

//    public ProductController(IProductService productService) {
//        this.productService = productService;
//    }

    @GetMapping("/products/{id}")
    public ProductDto getProductById(@PathVariable Long id) {

        Product product = productService.getProductById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setQuantity(product.getQuantity());
        productDto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }



}
