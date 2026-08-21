package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.models.Product;

public interface IProductService {

    Product getProductById(Long id);
}
