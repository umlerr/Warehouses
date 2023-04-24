package warehouses.Interface;

public class Manager {
    private String ID;
    private String Name;
    private String Surname;

    public Manager(String ID, String Name, String Surname) {
        this.ID = ID;
        this.Name = Name;
        this.Surname = Surname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String Surname) {
        this.Surname = Surname;
    }
}