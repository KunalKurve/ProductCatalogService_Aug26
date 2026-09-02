package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.TablePerClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "tpc_ta")
public class TA extends User {

    private int helpRequests;
}
