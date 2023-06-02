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

    @Column(name = "startdate")
    private LocalDate startdate;
    @Column(name = "enddate")
    private LocalDate enddate;
    @Column(name = "number")
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    public Integer getId_contract() {
        return id_contract;
    }

    public void setId_contract(Integer id_contract) {
        this.id_contract = id_contract;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
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
