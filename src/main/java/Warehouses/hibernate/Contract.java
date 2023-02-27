package Warehouses.hibernate;

import java.util.List;

public class Contract {
    private int ContractID;
    private List<Company> Companies;
    private String StartDate;
    private String EndDate;

    public int GetContractID(){
        return ContractID;
    }
    public void SetContractID(int ContractID){
        this.ContractID = ContractID;
    }

    public String GetStartDate(){
        return StartDate;
    }
    public void SetStartDate(String StartDate){
        this.StartDate = StartDate;
    }

    public String GetEndDate(){
        return EndDate;
    }
    public void SetEndDate(String EndDate){
        this.EndDate = EndDate;
    }

    public List<Company> GetListOfCompanies(){
        return Companies;
    }

    public static void main(String[] args) {

    }
}
