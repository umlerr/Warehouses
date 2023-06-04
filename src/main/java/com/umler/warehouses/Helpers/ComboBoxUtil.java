package com.umler.warehouses.Helpers;

import com.umler.warehouses.Model.Product;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

public class ComboBoxUtil {
    public static void configureNameComboBox(ComboBox<Product> nameComboBox) {
        nameComboBox.setCellFactory(param -> new ListCell<Product>() {
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

        nameComboBox.setConverter(new StringConverter<Product>() {
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
        typeComboBox.setCellFactory(param -> new ListCell<Product>() {
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

        typeComboBox.setConverter(new StringConverter<Product>() {
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