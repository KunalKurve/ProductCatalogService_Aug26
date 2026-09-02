package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.TablePerClass;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tpc_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {

    @Id
    private UUID id;
    private String name;
    private String email;

}
