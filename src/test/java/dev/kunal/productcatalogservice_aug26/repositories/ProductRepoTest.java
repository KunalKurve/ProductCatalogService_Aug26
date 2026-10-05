package dev.kunal.productcatalogservice_aug26.repositories;

import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductRepoTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    // not adding @Transactional because we want to commit the data into the database
    public void addDataIntoRDS() {

        Product product = new Product();
        product.setId(2L);
        product.setName("Iphone 18");
        product.setDescription("Iphone 18 is the latest and greatest smartphone from Apple.");
        product.setPrice(999.99);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryRepository.save(category);

        product.setCategory(category);
        productRepository.save(product);

    }

}
