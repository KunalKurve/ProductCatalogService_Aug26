package dev.kunal.productcatalogservice_aug26.TableInheritanceExamples.SingleTable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TA")
public class TA extends User {

    private int helpRequests;
}
