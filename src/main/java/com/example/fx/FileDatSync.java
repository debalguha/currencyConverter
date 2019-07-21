package com.example.fx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

public class FileDatSync implements DataSync {

    private final File fileToSyncTo;

    public FileDatSync(File fileToSyncTo) {
        this.fileToSyncTo = fileToSyncTo;
    }

    @Override
    public void accept(Stream<String> streamOfData) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(fileToSyncTo.getAbsoluteFile(), true))) {
            streamOfData.forEach(str -> writer.println(str));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
