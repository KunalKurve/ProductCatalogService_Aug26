package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.models.Product;
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

    public StorageProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
            throw new RuntimeException("Product with ID " + product.getId() + " already exists");
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
        productToUpdate.setLastUpdatedAt(new Date());
        return productRepository.save(productToUpdate);
    }

    @Override
    public void deleteProduct(Long id) {

        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isEmpty()){
            throw new RuntimeException("Product with ID " + id + " does not exist");
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
