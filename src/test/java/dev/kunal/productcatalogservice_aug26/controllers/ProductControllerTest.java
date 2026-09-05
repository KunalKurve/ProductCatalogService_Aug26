package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.services.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private IProductService productService;

    @Test
    public void TestGetProductById_WithValidProductId_ReturnProductSuccessfully() {
        Long productId = 5L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Iphone 17");

        when(productService.getProductDetailsById(productId)).thenReturn(product);

        var response = productController.getProductDetailsById(productId);

        assertNotNull(response);
        assertEquals(productId, response.getBody().getId());
        assertEquals("Iphone 17", response.getBody().getName());

        verify(productService, times(1)).getProductDetailsById(productId);
    }

    @Test
    public void TestGetProductById_WithNegativeId_ResultsInIllegalArgumentException() {
        Long productId = -5L;

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> productController.getProductDetailsById(productId)
        );

        assertEquals("Product ID must not be negative", exception.getMessage());
    }

    @Test
    public void TestGetProductById_WithIdEqualsToZero_ShouldThrowIllegalArgumentException() {
        // Arrange
        Long productId = 0L;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            productController.getProductDetailsById(productId)
        );

        assertEquals("Product ID must be greater than 0", exception.getMessage());

        verify(productService, times(0)).getProductDetailsById(productId);
    }

    @Test
    public void TestGetProductDetailsById_WithNonExistingProductId_ShouldThrowRuntimeException() {
         Long productId = 1000L; // Assuming this ID does not exist

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class,
                ()-> productController.getProductDetailsById(productId));

        // even this much test can be run without saving exception variable,
        // but we are saving it to check the exception message

        assertEquals("Product not found for ID: " + productId, exception.getMessage());

        verify(productService, times(1)).getProductDetailsById(productId);
    }

}