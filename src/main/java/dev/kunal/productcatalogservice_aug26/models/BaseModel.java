package dev.kunal.productcatalogservice_aug26.models;

import dev.kunal.productcatalogservice_aug26.models.enums.State;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {

    @Id
    private Long id;

    private Date createdAt;
    private Date lastUpdatedAt;

    private State state;
}
