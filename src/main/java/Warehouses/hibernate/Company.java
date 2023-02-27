package Warehouses.hibernate;

public class Company {
    private int CompanyID;
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String MSRN; // ОГРН
    private String TIN; // ИНН

    public int GetCompanyID(){
        return CompanyID;
    }
    public void SetCompanyID(int CompanyID){
        this.CompanyID = CompanyID;
    }

    public String GetCompanyName(){
        return Name;
    }
    public void SetCompanyName(String CompanyName){
        this.Name = CompanyName;
    }

    public String GetCompanyAddress(){
        return Address;
    }
    public void SetCompanyAddress(String CompanyAddress){
        this.Address = CompanyAddress;
    }

    public String GetCompanyPhoneNumber(){
        return PhoneNumber;
    }
    public void SetCompanyPhoneNumber(String CompanyPhoneNumber){
        this.PhoneNumber = CompanyPhoneNumber;
    }

    public String GetCompanyMSRN(){
        return MSRN;
    }
    public void SetCompanyMSRN(String CompanyMSRN){
        this.MSRN = CompanyMSRN;
    }

    public String GetCompanyTIN(){
        return TIN;
    }
    public void SetCompanyTIN(String CompanyTIN){
        this.TIN = CompanyTIN;
    }

    public static void main(String[] args) {

    }
}
