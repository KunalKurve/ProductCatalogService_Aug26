package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.clients.FakeStoreAPIClient;
import dev.kunal.productcatalogservice_aug26.dtos.FakeStoreProductDto;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.models.enums.State;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class FakeStoreProductService implements IProductService {

    //@Autowired
    //private RestTemplate restTemplate; in case only created config class
    // private RestTemplateBuilder restTemplateBuilder; // use if not created Client class
    @Autowired
    private FakeStoreAPIClient fakeStoreAPIClient;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Product getProductDetailsBasedOnUserRole(Long productId, Long userid) {
        return null;
    }

    @Override
    public Product getProductDetailsById(Long id) {

        /* Redis Pseudocode
        if found in redis:
            return;
        else
            fetch from Fake Store and
                save in Redis
         */

        FakeStoreProductDto fakeStoreProductDto = null;

        fakeStoreProductDto = (FakeStoreProductDto) redisTemplate.opsForHash().get("PRODUCTS", id);

        if (fakeStoreProductDto == null) {

            ResponseEntity<FakeStoreProductDto> responseEntity =
                    fakeStoreAPIClient.getProductDetailsById(id);

            // Add to Redis
            redisTemplate.opsForHash().put("PRODUCTS", id, responseEntity.getBody());

            if (fakeStoreAPIClient.validateResponse(responseEntity)) {
                System.out.println("Found by calling Fake Store");
                return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
            }
        }
        else {
            System.out.println("Found in Redis cache");
            return mapFakeStoreProductDtoToProduct(fakeStoreProductDto);
        }

        return null;

    }

    @Override
    public Product createProduct(Product product) {

        // RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto> responseEntity =
                fakeStoreAPIClient.postForEntity("https://fakestoreapi.com/products",
                        product,
                        FakeStoreProductDto.class
                );

        if (fakeStoreAPIClient.validateResponse(responseEntity)) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {

        ResponseEntity<FakeStoreProductDto> responseEntity = fakeStoreAPIClient.requestForEntity(
                "https://fakestoreapi.com/products/{id}",
                HttpMethod.PUT,
                product,
                FakeStoreProductDto.class,
                id
        );

        if (fakeStoreAPIClient.validateResponse(responseEntity)) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        ResponseEntity<FakeStoreProductDto> responseEntity = fakeStoreAPIClient.requestForEntity(
                "https://fakestoreapi.com/products/{id}",
                HttpMethod.PATCH,
                product,
                FakeStoreProductDto.class,
                id
        );

        if (fakeStoreAPIClient.validateResponse(responseEntity)) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public boolean deleteProduct(Long id) {

        // RestTemplate restTemplate = restTemplateBuilder.build();
        fakeStoreAPIClient.delete("https://fakestoreapi.com/products/{id}", id);

        return true;
    }

    @Override
    public List<Product> getAllProducts() {

        // RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto[]> responseEntity =
                fakeStoreAPIClient.requestForEntity("https://fakestoreapi.com/products",
                        HttpMethod.GET,
                        null,
                        FakeStoreProductDto[].class
                );

        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : responseEntity.getBody()){
            products.add(mapFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }

        return products;
    }


    public Product mapFakeStoreProductDtoToProduct (FakeStoreProductDto fakeStoreProductDto){

        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setQuantity(100); // Default quantity
        product.setImageUrl(fakeStoreProductDto.getImage());

        Optional<Category> optionalCategory = categoryRepository.findByName(fakeStoreProductDto.getCategory());
        if (optionalCategory.isPresent()) {
            product.setCategory(optionalCategory.get());
        }
        else {
            // If category does not exist
            Long id = 0L;
            Category category = new Category();
            category.setId(++id);
            category.setName(fakeStoreProductDto.getTitle());
            category.setDescription(fakeStoreProductDto.getDescription());
            category.setState(State.ACTIVE);
            category.setCreatedAt(new Date());
            category.setLastUpdatedAt(new Date());
            categoryRepository.save(category);
            product.setCategory(category);
        }

        return product;
    }


}



/*
FLOW OF PUT API
Our client  -> Product Dto

Our Service -> only talk in terms of  Models (Product)

FakeStore (3rd Party) -> FakeStoreProductDto


Our Client PUT to us : ProductDto
          ProductDto - to - Product (At controller)
Call Service layer method from Controller : Product
          Product - to - fakeStoreProductDto (At Service)
Call FakeStore  : FakeStoreProductDto

Response From FakeStore : FakeStoreProductDto
           FakeStoreProductDto - to - Product
send to controller from service : Product
           Product - to - ProductDto
send to our client from Controller : ProductDto


 */