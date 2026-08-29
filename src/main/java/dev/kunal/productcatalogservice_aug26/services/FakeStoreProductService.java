package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.FakeStoreProductDto;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.models.enums.State;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class FakeStoreProductService implements IProductService {

    @Autowired
    //private RestTemplate restTemplate; //How to create a bean - we will learn in future class
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product getProductById(Long id) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.getForEntity("https://fakestoreapi.com/products/{id}",
                        FakeStoreProductDto.class,
                        id
                );

        if (responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }
            return null;


    }

    @Override
    public Product createProduct(Product product) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.postForEntity("https://fakestoreapi.com/products",
                        product,
                        FakeStoreProductDto.class
                );

        if (responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {

        ResponseEntity<FakeStoreProductDto> responseEntity = requestForEntity(
                "https://fakestoreapi.com/products/{id}",
                HttpMethod.PUT,
                product,
                FakeStoreProductDto.class,
                id
        );

        if (responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        ResponseEntity<FakeStoreProductDto> responseEntity = requestForEntity(
                "https://fakestoreapi.com/products/{id}",
                HttpMethod.PATCH,
                product,
                FakeStoreProductDto.class,
                id
        );

        if (responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }

        return null;
    }

    @Override
    public void deleteProduct(Long id) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete("https://fakestoreapi.com/products/{id}", id);

    }

    @Override
    public List<Product> getAllProducts() {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto[]> responseEntity =
                restTemplate.getForEntity("https://fakestoreapi.com/products",
                        FakeStoreProductDto[].class
                );

        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : responseEntity.getBody()){
            products.add(mapFakeStoreProductDtoToProduct(fakeStoreProductDto));
        }

        return products;
    }

    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request,
                                               Class<T> responseType, Object... uriVariables) throws RestClientException {

        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
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
            Category category = new Category();
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