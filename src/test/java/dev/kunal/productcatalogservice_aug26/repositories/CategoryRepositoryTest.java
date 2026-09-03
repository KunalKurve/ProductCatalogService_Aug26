package dev.kunal.productcatalogservice_aug26.repositories;

import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void testFetchTypes() {
        Optional<Category> categoryOptional = categoryRepository.findById(1L); //hardcoded 1 row that's why didn't see subquery
        Category category = categoryOptional.get();
//        for(Product product : category.getProducts()) {
//            System.out.println(product.getName());
//        }
    }


    @Test
    @Transactional
    public void testSubSelectFetchMode() {
        List<Category> allCategories = categoryRepository.findAll();
        for(Category category : allCategories) {
            for (Product product : category.getProducts()) {
                System.out.println(product.getName());
            }
        }
    }
}