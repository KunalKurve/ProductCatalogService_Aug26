package dev.kunal.productcatalogservice_aug26.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel {

    private String name;

    // If you keep cascade = CascadeType.ALL, then when you delete a product, it will also delete the category.
    // and because Category model has cascade = CascadeType.ALL on products, it will delete all products in that category.
    // so we should not use cascade = CascadeType.ALL here.
    // @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private Category category;

    private String description;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    private Boolean isSaleEligible; //This is why we require dto

}
