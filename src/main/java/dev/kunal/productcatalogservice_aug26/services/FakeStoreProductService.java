package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.FakeStoreProductDto;
import dev.kunal.productcatalogservice_aug26.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FakeStoreProductService implements IProductService{

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Product getProductById(Long id) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/{id}",
                FakeStoreProductDto.class,
                id
        );

//        Product product = new Product();
//        product.setId(id);
//        product.setName("Sample Product");
//        product.setDescription("This is a sample product description.");
//        product.setPrice(19.99);
//        product.setQuantity(100);
//        product.setImageUrl("https://example.com/sample-product.jpg");
//        return product;

        FakeStoreProductDto fakeStoreProductDto = responseEntity.getBody();
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        product.setQuantity(100);
        if(fakeStoreProductDto.getCategory() != null) {

        }
        return product;
    }


}
