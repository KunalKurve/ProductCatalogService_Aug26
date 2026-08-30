package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.exceptions.ProductAlreadyExistsException;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import dev.kunal.productcatalogservice_aug26.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class StorageProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public StorageProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long id) {

        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElse(null);

    }

    @Override
    public Product createProduct(Product product) {

        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if(existingProduct.isPresent()){
            throw new ProductAlreadyExistsException("Product with ID " + product.getId() + " already exists");
        }
        // if category is a new category, we need to save it first before saving the product
        Optional<Category> existingCategory = categoryRepository.findById(product.getCategory().getId());
        if (existingCategory.isEmpty()) {
            categoryRepository.save(product.getCategory());
        }

        return productRepository.save(product);
    }

    @Override
    public Product replaceProduct(Long id, Product product) {

        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if(existingProduct.isEmpty()){
            throw new RuntimeException("Product with ID " + product.getId() + " does not exist");
        }

        Product productToUpdate = existingProduct.get();
        productToUpdate.setId(product.getId());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setQuantity(product.getQuantity());
        productToUpdate.setImageUrl(product.getImageUrl());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setLastUpdatedAt(new Date());
        return productRepository.save(productToUpdate);
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if(existingProduct.isEmpty()){
            throw new RuntimeException("Product with ID " + product.getId() + " does not exist");
        }

        Product productToUpdate = existingProduct.get();
        productToUpdate.setId(product.getId());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setQuantity(product.getQuantity());
        productToUpdate.setImageUrl(product.getImageUrl());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setLastUpdatedAt(new Date());
        return productRepository.save(productToUpdate);
    }

    @Override
    public boolean deleteProduct(Long id) {

        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isEmpty()){
            throw new RuntimeException("Product with ID " + id + " does not exist");
        }

        productRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
