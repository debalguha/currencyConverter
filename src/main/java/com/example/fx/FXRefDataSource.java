package com.example.fx;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public interface FXRefDataSource extends DataSource<Collection<FXEntry>> {
    Stream<FXEntry> getFxValueEntries();
    void close() throws IOException;
}
