package warehouses.Interface;

public class Manager {
    private String ID;
    private String Name;
    private String Surname;

    public Manager(String id, String name, String surname) {
        ID = id;
        Name = name;
        Surname = surname;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

}