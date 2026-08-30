package dev.kunal.productcatalogservice_aug26.repositories;

import dev.kunal.productcatalogservice_aug26.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Product save(Product product);

    void deleteById(Long id);

    //If your Product entity doesn't have title, Spring cannot create the repository query.
    // Page<Product> findProductByTitle(String query, Pageable pageable);



    //Custom Queries

//    List<Product> findProductByPriceBetween(Double low, Double high);
//
//    List<Product> findAllByIsPrime(Boolean val);
//    List<Product> findAllByIsPrimeTrue();
//
//    List<Product> findAllByOrderByPriceDesc();
//
//    @Query("SELECT p.title from Product p WHERE p.id=?1")
//    String findProductNameById(Long id);
//
//    @Query("SELECT c.title from Product p join Category c on p.category.id=c.id WHERE p.id=:id")
//    String findCategoryNameFromProductId(@Param("id") Long id);
}
