package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.UserDto;
import dev.kunal.productcatalogservice_aug26.exceptions.ProductAlreadyExistsException;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.models.enums.Scope;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import dev.kunal.productcatalogservice_aug26.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class StorageProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;
    // RestTemplateBuilder is not used in production apps

    public StorageProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductDetailsBasedOnUserRole(Long productId, Long userid) {

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) return null;

        if(productOptional.get().getScope().equals(Scope.PUBLIC)){
            return productOptional.get();
        }

        // We are calling UserService using ServiceDiscovery (EurekaServer/Spring Cloud)
        ResponseEntity<UserDto> response = restTemplate.getForEntity(
                "http://userservice/users/{userId}",
                UserDto.class,
                userid
        );

        if(response.hasBody() && response.getBody() != null && response.getStatusCode().is2xxSuccessful()){
            UserDto userDto = response.getBody();
            if (userDto.getRoles().contains("ADMIN") || userDto.getRoles().contains("SELLER")){
                return productOptional.get();
            }
        }

        return null;
    }

    @Override
    public Product getProductDetailsById(Long id) {

        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElse(null);

    }

    @Override
    public Product createProduct(Product product) {

        if(product.getId() != null) {
            Optional<Product> existingProduct = productRepository.findById(product.getId());
            if (existingProduct.isPresent()) {
                throw new ProductAlreadyExistsException("Product with ID " + product.getId() + " already exists");
            }
            // if category is a new category, we need to save it first before saving the product
            Optional<Category> existingCategory = categoryRepository.findById(product.getCategory().getId());
            if (existingCategory.isEmpty()) {
                categoryRepository.save(product.getCategory());
            }
        }
        if(product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        Optional<Product> existingProduct = productRepository.findByName(product.getName());
        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException("Product with name " + product.getName() + " already exists");
        }

        // we are not auto-generating the ID, so we need to check if the ID is already present in the database
        // so in Request Body, we are sending the ID
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
        // how can I return false here?
        if (existingProduct.get().getCategory() == null) {
            return false;
        }
        productRepository.deleteById(id);
        return true;

        // how would controller know if row is deleted if we keep delete return type as void?
        // return T/F
        // throw exception and catch
        // in try-catch
        // database exception hi aayega, untill unless multithreading
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
