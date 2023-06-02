package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Company")
public class Company {
    @Id
    @Column(name = "id_company")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_company;
    @Column(name = "name")
    private String Name;
    @Column(name = "address")
    private String Address;
    @Column(name = "phone")
    private String PhoneNumber;
    @Column(name = "tin")
    private String TIN; // ИНН


    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Contract> contractList = new ArrayList<>();


    public void addContract(Contract contract) {
        contractList.add(contract);
        contract.setCompany(this);
    }
    public void removeClient(Contract contract) {
        contractList.remove(contract);
        contract.setCompany(null);
    }

    public Integer getId_company() {
        return id_company;
    }

    public void setId_company(Integer id_company) {
        this.id_company = id_company;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(List<Contract> contractList) {
        this.contractList = contractList;
    }

    @Override
    public String toString() {
        return String.format("%s", this.Name);
    }
}
