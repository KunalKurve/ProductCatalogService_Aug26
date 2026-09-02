package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.SingleTable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Instructor")
public class Instructor extends User {

    private String company;
}
