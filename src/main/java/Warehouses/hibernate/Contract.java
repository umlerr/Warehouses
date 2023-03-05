package Warehouses.hibernate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Contracts")

public class Contract {
    @Id
    @Column(name = "ContractID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ContractID;
    @OneToOne
    @JoinColumn(name = "CompanyId")
    private Company CompanyInfo;
    @Column(name = "StartDate")
    private String StartDate;
    @Column(name = "EndDate")
    private String EndDate;
    @ManyToOne
    @JoinColumn(name = "WarehouseID")
    private Warehouse WarehouseID;
    public Warehouse getWarehouseID(){
        return this.WarehouseID;
    }
    public void setWarehouseID(Warehouse WarehouseID){
        this.WarehouseID = WarehouseID;
    }

    public Company getCompanyInfo(){
        return this.CompanyInfo;
    }
    public void setCompanyInfo(Company CompanyInfo){
        this.CompanyInfo = CompanyInfo;
    }
    public int getContractID(){
        return ContractID;
    }
    public void setContractID(int ContractID){
        this.ContractID = ContractID;
    }
    public String getStartDate(){
        return StartDate;
    }
    public void setStartDate(String StartDate){
        this.StartDate = StartDate;
    }

    public String getEndDate(){
        return EndDate;
    }
    public void setEndDate(String EndDate){
        this.EndDate = EndDate;
    }

    public static void main(String[] args) {

    }
}
