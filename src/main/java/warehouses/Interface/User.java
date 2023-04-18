package warehouses.Interface;

public class User {
    private String ID;
    private String Name;
    private String Surname;
    private String Age;

    public User(String id, String name, String surname, String age) {
        Name = name;
        Surname = surname;
        Age = age;
        ID = id;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getAge() {
        return Age;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setAge(String age) {
        Age = age;
    }
}