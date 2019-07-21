package com.example.fx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileFXRefDataSource implements DataSource<Collection<FXEntry>> {

    private final Iterator<String> lineIterator;
    private final String[] headers;
    private final BufferedReader fileBuffered;

    public static DataSource<Collection<FXEntry>> fromRefDataFile(File refDataFile) {
        return new FileFXRefDataSource(refDataFile);
    }

    public FileFXRefDataSource(File fileToSeedFrom) {
        Objects.requireNonNull(fileToSeedFrom, "Can not generate data source with a null file");
        if (!fileToSeedFrom.exists() || !fileToSeedFrom.canRead()) {
            throw new IllegalArgumentException("The file specified is either does not exist or unreadable!!");
        }
        try {
            fileBuffered = new BufferedReader(new FileReader(fileToSeedFrom));
            headers = fileBuffered.readLine().split(",", -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lineIterator = fileBuffered.lines().iterator();
    }

    @Override
    public Iterator<Collection<FXEntry>> iterator() {
        return new FXEntryIterator();
    }

    @Override
    public Stream<Collection<FXEntry>> getData() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void close() throws IOException {
        fileBuffered.close();
    }

    class FXEntryIterator implements Iterator<Collection<FXEntry>> {

        @Override
        public boolean hasNext() {
            return lineIterator.hasNext();
        }

        @Override
        public Collection<FXEntry> next() {
            String[] elements = lineIterator.next().split(",", -1);
            String fromCurrency = elements[0];
            return IntStream.range(1, elements.length).mapToObj((index) -> new FXEntry(fromCurrency, headers[index], elements[index])).collect(Collectors.toList());
        }
    }
}
