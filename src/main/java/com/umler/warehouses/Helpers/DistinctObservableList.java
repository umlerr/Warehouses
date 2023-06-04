package com.umler.warehouses.Helpers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class DistinctObservableList<T> {

    private final ObservableList<T> sourceList;
    private final FilteredList<T> filteredList;

    public DistinctObservableList(ObservableList<T> list) {
        sourceList = list;
        filteredList = new FilteredList<>(sourceList);
    }

    public void distinct(Function<? super T, ?> keyExtractor) {
        filteredList.setPredicate(distinctByKey(keyExtractor));
    }

    private <U> Function<T, U> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return keyExtractor::apply;
    }

    private <U> Predicate<T> distinctByKey(Function<? super T, ? extends U> keyExtractor) {
        Set<U> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public ObservableList<T> getFilteredList() {
        return filteredList;
    }

}