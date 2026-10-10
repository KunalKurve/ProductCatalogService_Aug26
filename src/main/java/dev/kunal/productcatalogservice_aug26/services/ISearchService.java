package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.SortParams;
import dev.kunal.productcatalogservice_aug26.models.Product;

import java.util.List;

public interface ISearchService {

    List<Product> searchProducts(String query, Integer pageSize, Integer pageNumber);

    List<Product> searchAndSortProducts(String query, Integer pageSize, Integer pageNumber, List<SortParams> sortParams);
}
