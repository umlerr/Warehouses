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
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "tin")
    private String tin; // ИНН


    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Contract> contractList = new ArrayList<>();


    public void addProduct(Product product) {
        productList.add(product);
        product.setCompany(this);
    }
    public void removeProduct(Contract product) {
        productList.remove(product);
        product.setCompany(null);
    }

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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
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
        return String.format("%s", this.name);
    }
}
