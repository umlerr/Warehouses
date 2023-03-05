package Warehouses.hibernate;

public class Company {
    private int CompanyID;
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String MSRN; // ОГРН
    private String TIN; // ИНН

    public int getCompanyID(){
        return CompanyID;
    }
    public void setCompanyID(int CompanyID){
        this.CompanyID = CompanyID;
    }

    public String getName(){
        return Name;
    }
    public void setName(String CompanyName){
        this.Name = CompanyName;
    }

    public String getAddress(){
        return Address;
    }
    public void setAddress(String CompanyAddress){
        this.Address = CompanyAddress;
    }

    public String getPhoneNumber(){
        return PhoneNumber;
    }
    public void setPhoneNumber(String CompanyPhoneNumber){
        this.PhoneNumber = CompanyPhoneNumber;
    }

    public String getMSRN(){
        return MSRN;
    }
    public void setMSRN(String CompanyMSRN){
        this.MSRN = CompanyMSRN;
    }

    public String getTIN(){
        return TIN;
    }
    public void setTIN(String CompanyTIN){
        this.TIN = CompanyTIN;
    }

    public static void main(String[] args) {

    }
}
