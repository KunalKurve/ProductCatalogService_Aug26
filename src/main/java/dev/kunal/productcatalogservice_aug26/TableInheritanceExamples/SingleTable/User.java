package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.SingleTable;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    private UUID id;
    private String name;
    private String email;

}
