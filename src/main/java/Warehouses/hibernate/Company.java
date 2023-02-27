package Warehouses.hibernate;

public class Company {
    private String Name;
    private String Address;
    private String PhoneNumber;
    private String MSRN; // ОГРН
    private String TIN; // ИНН
    public String GetInfo() {
        /**
         * @return Информация о компании
         */
        return("1");
    }
    /**
     * @param Info Информация о компании
     */
    public void SetInfo(String Info) {

    }
    public static void main(String[] args) {

    }
}
