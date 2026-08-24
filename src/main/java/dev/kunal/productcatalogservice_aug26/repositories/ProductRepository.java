package dev.kunal.productcatalogservice_aug26.repositories;

import dev.kunal.productcatalogservice_aug26.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
