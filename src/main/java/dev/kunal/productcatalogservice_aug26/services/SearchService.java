package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.SortParams;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService implements ISearchService{

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> searchProducts(String query, Integer pageSize, Integer pageNumber) {

        // if we want to sort the results by Price, we can use the following code
        Sort sortByPriceDesc = Sort.by("price").descending();
        Sort sortByIdAsc = Sort.by(Sort.Direction.ASC, "id");
        Sort sort = sortByPriceDesc.and(sortByIdAsc);

        // return productRepository.findByNameContainingIgnoreCase(query, PageRequest.of(pageNumber, pageSize)).getContent();
         return productRepository.findByName(query, PageRequest.of(pageNumber, pageSize, sort)).getContent();

    }

    // Homework:
    // Implement the searchAndSortProducts method to sort the results based on the provided sorting parameters.
    @Override
    public List<Product> searchAndSortProducts(String query, Integer pageSize, Integer pageNumber, List<SortParams> sortParams) {

        Sort sort = Sort.unsorted();
        for (SortParams param : sortParams) {
            Sort newSort = Sort.by(param.getField());
            if (param.getDirection().equalsIgnoreCase("desc")) {
                newSort = newSort.descending();
            } else {
                newSort = newSort.ascending();
            }
            sort = sort.and(newSort);
        }

        return productRepository.findByNameContainingIgnoreCase(query, PageRequest.of(pageNumber, pageSize, sort)).getContent();
    }


}
