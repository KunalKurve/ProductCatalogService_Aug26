package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.dtos.CategoryDto;
import dev.kunal.productcatalogservice_aug26.dtos.FakeStoreProductDto;
import dev.kunal.productcatalogservice_aug26.dtos.ProductDto;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import dev.kunal.productcatalogservice_aug26.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;

//    public ProductController(@Qualifier("storageProductService") IProductService productService) {
//        this.productService = productService;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductDetailsById(@PathVariable("id") Long productId) {

        if(productId == 0){
            //return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            throw new IllegalArgumentException("Product ID must be greater than 0");
        }
        else if (productId < 0) {
            //return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            throw new IllegalArgumentException("Product ID must not be negative");
        }

        Product product = productService.getProductDetailsById(productId);
        if(product == null){
            //return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            throw new RuntimeException("Product not found for ID: " + productId);
        }

        ProductDto productDto = mapProductToProductDto(product);
        return ResponseEntity.ok(productDto);
        // return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts() {

        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = products.stream()
                .map(this::mapProductToProductDto)
                .toList();

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    //POST
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product newProduct = productService.createProduct(mapProductDtoToProduct(productDto));
        ProductDto createdProductDto = mapProductToProductDto(newProduct);
        return new ResponseEntity<>(createdProductDto, HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable(name = "id") Long productId, @RequestBody ProductDto productDto){

        Product updatedProduct = productService.replaceProduct(productId, mapProductDtoToProduct(productDto));
        ProductDto updatedProductDto = mapProductToProductDto(updatedProduct);
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }

    //PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long productId, @RequestBody ProductDto productDto){

        Product updatedProduct = productService.updateProduct(productId, mapProductDtoToProduct(productDto));
        ProductDto updatedProductDto = mapProductToProductDto(updatedProduct);
        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long productId){
        // Delete the product and return a response
        boolean isDeleted = productService.deleteProduct(productId);
        if(!isDeleted){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            // If the product is not deleted, it means there was an error.
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Product mapProductDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        if (productDto.getCategory() != null) {
            CategoryDto categoryDto = productDto.getCategory();
            Optional<Category> categoryOptional = categoryRepository.findById(categoryDto.getId());
            if (categoryOptional.isPresent()) {
                product.setCategory(categoryOptional.get());
            } else {
                Category category = new Category();
                category.setId(categoryDto.getId());
                category.setName(categoryDto.getName());
                category.setDescription(categoryDto.getDescription());
                product.setCategory(category);
            }
        }
        return product;
    }


    public ProductDto mapProductToProductDto(Product product) {
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

/*
bean =  singleton object whose lifecycle is being controller by Spring
If you want to tell spring to create a bean of any class - you just annotate that class
with some annotation like @RestController or @Service or @Component or @Configuration

Spring context is the name of the bowl or  vessel or container in which spring
store all the beans.
 */