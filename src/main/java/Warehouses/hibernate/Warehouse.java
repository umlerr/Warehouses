package Warehouses.hibernate;

import java.lang.String;
import java.util.*;

public class Warehouse {
    private String Address;
    private ArrayList<Contract> Contracts;
    private ArrayList<Manager> Managers;
    private ArrayList<Room> Rooms;

    public String GetAddress(){
        /**
         * @return Адрес склада
         */
        return("1");
    }
    public String GetContractList(){
        /**
         * @return Перечень договоров
         */
        return("1");
    }
    public String GetManagersList(){
        /**
         * @return Список диспетчеров
         */
        return("1");
    }
    public String GetRoomsList(){
        /**
         * @return Список помещений
         */
        return("1");
    }
    public String GetFullness(){
        /**
         * @return Заполненность склада
         */
        return("1");
    }
    public String GetTypeNumber(){
        /**
         * @return Заполненность склада
         */
        return("1");
    }
    /**
     * @param Info Данные о складе
     */
    public void SetInfo(String Info){
        /**
         * @return Установка\изменения данных класса Warehouse
         */
    }
    public static void main(String[] args) {

    }
}
