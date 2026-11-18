package dev.kunal.productcatalogservice_aug26.dtos;

import java.util.List;

public record ImportResult(int imported, int failed, List<ImportError> errors) {
}
