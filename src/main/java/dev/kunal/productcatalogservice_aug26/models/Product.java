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

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    private String description;

    private Double price;

    private Integer quantity;

    private String imageUrl;

    private Boolean isSaleEligible; //This is why we require dto

}
