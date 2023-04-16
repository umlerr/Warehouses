package oop.Interface;

public class User {
    private String Name;
    private String Surname;
    private Integer Age;
    private Integer ID;


    public User(String name, String surname, Integer age, Integer id) {
        Name = name;
        Surname = surname;
        Age = age;
        ID = id;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public Integer getAge() {
        return Age;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setAge(Integer age) {
        Age = age;
    }
}