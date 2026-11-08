package dev.kunal.productcatalogservice_aug26.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FakeStoreProductDto implements Serializable {

    private Long id;
    private String title;
    private Double price;
    private String description;
    private String image;
    private String category;
}
