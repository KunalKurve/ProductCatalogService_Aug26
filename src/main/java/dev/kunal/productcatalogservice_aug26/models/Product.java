package dev.kunal.productcatalogservice_aug26.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseModel {

    private String name;
    private Category category;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private Boolean isSaleEligible;

}
