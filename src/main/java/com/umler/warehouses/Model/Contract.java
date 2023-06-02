package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Contract")

public class Contract {
    @Id
    @Column(name = "id_contract")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_contract;

    @Column(name = "StartDate")
    private LocalDate StartDate;
    @Column(name = "EndDate")
    private LocalDate EndDate;
    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Company company;


    public Integer getId_contract() {
        return id_contract;
    }

    public void setId_contract(Integer id_contract) {
        this.id_contract = id_contract;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
