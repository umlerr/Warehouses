module sample{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;
    requires org.apache.logging.log4j.slf4j;
    requires slf4j.api;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens com.umler.warehouses to javafx.fxml;
    exports com.umler.warehouses;

    exports com.umler.warehouses.Controllers;
    opens com.umler.warehouses.Controllers;

    exports com.umler.warehouses.Model;
    opens com.umler.warehouses.Model;

    exports com.umler.warehouses.Enums;
    opens com.umler.warehouses.Enums;

    exports com.umler.warehouses.Converters;
    opens com.umler.warehouses.Converters;

    exports com.umler.warehouses.Helpers;
    opens com.umler.warehouses.Helpers;

    exports com.umler.warehouses.Services;
    opens com.umler.warehouses.Services;

    exports com.umler.warehouses.AppClasses;
    opens com.umler.warehouses.AppClasses;

    exports com.umler.warehouses.AddControllers;
    opens com.umler.warehouses.AddControllers;

}