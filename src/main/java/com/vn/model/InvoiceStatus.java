package com.vn.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "invoiceStatus")
public class InvoiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idStatus")
    private Long idStatus;

    @Column(name = "statusName")
    private String statusName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceStatus")
    private Set<Invoice> invoiceSet;

    @Override
    public String toString() {
        return statusName;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Set<Invoice> getInvoiceSet() {
        return invoiceSet;
    }

    public void setInvoiceSet(Set<Invoice> invoiceSet) {
        this.invoiceSet = invoiceSet;
    }
}
