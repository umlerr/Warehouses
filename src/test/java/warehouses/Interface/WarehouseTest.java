package warehouses.Interface;

import com.umler.warehouses.main_app.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
class WarehouseTest {

    private static final Logger logger = LoggerFactory.getLogger("LoginScene Logger");

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
        assertEquals(2,warehouse.getID());
    }

    @Test
    void getAddress() {
        assertEquals("ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, Россия, 197022", warehouse.getAddress());
    }
}