package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.TablePerClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "tpc_instructor")
public class Instructor extends User {

    private String company;
}
