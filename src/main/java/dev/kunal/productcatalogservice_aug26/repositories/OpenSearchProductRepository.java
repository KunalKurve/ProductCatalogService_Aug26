package dev.kunal.productcatalogservice_aug26.repositories;

import dev.kunal.productcatalogservice_aug26.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.net.URLConnection;
import java.util.Optional;

public interface OpenSearchProductRepository extends ElasticsearchRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long id);

    Iterable<Product> findAllByDescriptionAndName(String description, String name);

    Optional<Product> findAllByDescriptionContainingOrNameContainingOrNameContaining(String desc, String name);

    Slice<Product> findByName(String query, PageRequest pageable);

    Page<Product> findByNameContainingIgnoreCase(String query, Pageable pageable);
}
