package dev.kunal.productcatalogservice_aug26.controllers;

import dev.kunal.productcatalogservice_aug26.dtos.ImportResult;
import dev.kunal.productcatalogservice_aug26.services.CategoryImportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/categories")
public class CategoryImportController {

    private final CategoryImportService importService;

    public CategoryImportController(CategoryImportService importService) {
        this.importService = importService;
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ImportResult> importCategories(
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("CSV file cannot be empty");
        }

        return ResponseEntity.ok(
                importService.importCsv(file.getInputStream())
        );
    }
}