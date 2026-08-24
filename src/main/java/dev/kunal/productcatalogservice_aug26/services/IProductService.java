package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.models.Product;

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
}
