package dev.kunal.productcatalogservice_aug26.models;

import dev.kunal.productcatalogservice_aug26.models.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Id
    private Long id;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date lastUpdatedAt;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    private boolean isPrivate;
}
