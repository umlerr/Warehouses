package com.umler.warehouses.Helpers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Класс для отбрасывания повторов с списке с типом объекта.
 * Используется для корректного отображения ComboBox, для вывода разных параметров одного класса в разные ComboBox.
 * @author Umler
 */

public class DistinctObservableList<T> {

    private final FilteredList<T> filteredList;

    public DistinctObservableList(ObservableList<T> list) {
        filteredList = new FilteredList<>(list);
    }

    public void distinct(Function<? super T, ?> keyExtractor) {
        filteredList.setPredicate(distinctByKey(keyExtractor));
    }

    private <U> Predicate<T> distinctByKey(Function<? super T, ? extends U> keyExtractor) {
        Set<U> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public ObservableList<T> getFilteredList() {
        return filteredList;
    }

}