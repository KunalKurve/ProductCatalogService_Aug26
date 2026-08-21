package dev.kunal.productcatalogservice_aug26.dtos;

import dev.kunal.productcatalogservice_aug26.models.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private CategoryDto category;
    private String imageUrl;
}
