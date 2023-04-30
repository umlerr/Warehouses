package warehouses.Interface;

import com.umler.warehouses.main_app.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class WarehouseTest {

    Warehouse warehouse = new Warehouse(1,"ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, Россия, 197022");

    @BeforeEach
    void setUp() {
        System.out.println("Запуск теста");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Конец теста");
    }

    @Test
    void getID() {
        assertEquals(1,warehouse.getID());
    }

    @Test
    void getAddress() {
        assertEquals("ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, Россия, 197022", warehouse.getAddress());
    }
}