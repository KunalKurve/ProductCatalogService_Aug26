package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.models.Product;

import java.util.List;

public interface IProductService {

    //GET
    Product getProductById(Long id);

    //POST
    Product createProduct(Product product);

    //PUT
    Product replaceProduct(Long id, Product product);

    //PATCH
    Product updateProduct(Long id, Product product);

    //DELETE
    void deleteProduct(Long id);

    // GET all products
    List<Product> getAllProducts();
}
