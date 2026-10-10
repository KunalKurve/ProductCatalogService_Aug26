package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.dtos.SearchRequestDto;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @PostMapping
    public List<Product> searchProducts(@RequestBody SearchRequestDto searchRequestDto) {

        return searchService.searchProducts(
                searchRequestDto.getQuery(),
                searchRequestDto.getPageSize(),
                searchRequestDto.getPageNumber()
        );
    }

    @PostMapping("/sort")
    public List<Product> searchAndSortProducts(@RequestBody SearchRequestDto searchRequestDto) {

        return searchService.searchAndSortProducts(
                searchRequestDto.getQuery(),
                searchRequestDto.getPageSize(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getSortParams()
        );
    }

}
