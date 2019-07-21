package com.example.fx;

import java.util.Collection;
import java.util.stream.Stream;

public interface FXRefDataSource extends Iterable<Collection<FXEntry>>{
    Stream<FXEntry> getFxValueEntries();
}
