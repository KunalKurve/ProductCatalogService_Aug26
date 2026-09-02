package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.JoinedTable;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "jt_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    private UUID id;
    private String name;
    private String email;

}
