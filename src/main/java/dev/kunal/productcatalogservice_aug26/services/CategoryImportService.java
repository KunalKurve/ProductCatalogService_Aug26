package dev.kunal.productcatalogservice_aug26.services;

import dev.kunal.productcatalogservice_aug26.dtos.ImportError;
import dev.kunal.productcatalogservice_aug26.dtos.ImportResult;
import dev.kunal.productcatalogservice_aug26.models.Category;
import dev.kunal.productcatalogservice_aug26.models.enums.State;
import dev.kunal.productcatalogservice_aug26.repositories.CategoryRepository;
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
public class CategoryImportService {

    private static final int BATCH_SIZE = 500;

    private final CategoryRepository categoryRepository;

    public CategoryImportService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ImportResult importCsv(InputStream inputStream) throws IOException {
        int imported = 0;
        int failed = 0;
        List<Category> batch = new ArrayList<>();
        List<ImportError> errors = new ArrayList<>();

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
                    Long id = Long.valueOf(row.get("id"));
                    String name = row.get("name").trim();

                    if (name.isEmpty()) {
                        throw new IllegalArgumentException("Category name cannot be empty");
                    }

                    if (categoryRepository.findById(id).isPresent()) {
                        throw new IllegalArgumentException("Category with ID " + id + " already exists");
                    }

                    if (categoryRepository.findByName(name).isPresent()) {
                        throw new IllegalArgumentException("Category with name " + name + " already exists");
                    }

                    Category category = new Category();
                    category.setId(id);
                    category.setName(name);
                    category.setDescription(row.get("description"));
                    category.setState(State.ACTIVE);

                    batch.add(category);

                    if (batch.size() == BATCH_SIZE) {
                        categoryRepository.saveAll(batch);
                        categoryRepository.flush();
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
                categoryRepository.saveAll(batch);
                categoryRepository.flush();
                imported += batch.size();
            }
        }

        return new ImportResult(imported, failed, errors);
    }
}