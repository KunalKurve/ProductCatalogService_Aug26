package dev.kunal.productcatalogservice_aug26.models;

import dev.kunal.productcatalogservice_aug26.models.enums.State;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseModel {

    private Long id;

    private Date createdAt;
    private Date lastUpdatedAt;

    private State state;
}
