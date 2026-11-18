package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.ImportError;
import dev.kunal.productcatalogservice_aug26.dtos.ImportResult;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.Product;
import dev.kunal.productcatalogservice_aug26.models.enums.Scope;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
import dev.kunal.productcatalogservice_aug26.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImportService {

    private static final int BATCH_SIZE = 500;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    public ProductImportService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            EntityManager entityManager) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public ImportResult importCsv(InputStream inputStream) throws IOException {
        int imported = 0;
        int failed = 0;
        List<ImportError> errors = new ArrayList<>();
        List<Product> batch = new ArrayList<>();

        try (
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreEmptyLines(true)
                        .build()
                        .parse(reader)
        ) {
            for (CSVRecord row : parser) {
                try {
                    Product product = mapRow(row);
                    batch.add(product);

                    if (batch.size() == BATCH_SIZE) {
                        productRepository.saveAll(batch);
                        productRepository.flush();
                        entityManager.clear();

                        imported += batch.size();
                        batch.clear();
                    }
                } catch (RuntimeException exception) {
                    failed++;
                    errors.add(new ImportError(
                            (int) row.getRecordNumber(),
                            exception.getMessage()
                    ));
                }
            }

            if (!batch.isEmpty()) {
                productRepository.saveAll(batch);
                productRepository.flush();
                imported += batch.size();
            }
        }

        return new ImportResult(imported, failed, errors);
    }

    private Product mapRow(CSVRecord row) {
        Product product = new Product();

        product.setId(Long.valueOf(row.get("id")));
        product.setName(row.get("name"));
        product.setDescription(row.get("description"));
        product.setPrice(Double.valueOf(row.get("price")));
        product.setQuantity(Integer.valueOf(row.get("quantity")));
        product.setImageUrl(row.get("imageUrl"));
        product.setScope(
                "PRIVATE".equalsIgnoreCase(row.get("scope"))
                        ? Scope.PRIVATE
                        : Scope.PUBLIC
        );

        Long categoryId = Long.valueOf(row.get("categoryId"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category not found: " + categoryId));

        product.setCategory(category);
        return product;
    }
}
