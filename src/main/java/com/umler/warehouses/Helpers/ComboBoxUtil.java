package com.umler.warehouses.Helpers;

import com.umler.warehouses.Model.Product;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

/**
 * Класс для переопределения вида ComboBox,
 * для вывода разных параметров одного класса в разные ComboBox.
 * @author Umler
 */

public class ComboBoxUtil {
    public static void configureNameComboBox(ComboBox<Product> nameComboBox) {
        nameComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getName());
                }
            }
        });

        nameComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Product product) {
                return product == null ? "" : product.getName();
            }

            @Override
            public Product fromString(String string) {
                return null;
            }
        });
    }

    public static void configureTypeComboBox(ComboBox<Product> typeComboBox) {
        typeComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.getType());
                }
            }
        });

        typeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Product product) {
                return product == null ? "" : product.getType();
            }

            @Override
            public Product fromString(String string) {
                return null;
            }
        });
    }
}