package com.example.fx;

import com.example.fx.conversion.Converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConvertDataFileByFXrate {
    public static void main(String args[]) throws Exception {
        File readDataFile = new File(Thread.currentThread().getContextClassLoader().getResource("data.csv").getFile());
        File refDataFile = new File(Thread.currentThread().getContextClassLoader().getResource("ref.csv").getFile());
        File writeDataFile = new File(Thread.currentThread().getContextClassLoader().getResource("output.csv").getFile());
        writeDataFile.createNewFile();

        refDataLookupTable(refDataFile);



    }

    private static Map<Pair<String, String>, Converter> refDataLookupTable(File refDataFile) throws IOException{
        return LookupTableBuilderV2.withRefDataSource(
                FileFXRefDataSource.fromRefDataFile(refDataFile)
        ).buildFXConversionTable();
    }

    class DataFileWithHeaderIterator {
        private final String[] headers;
        private final BufferedReader dataFileReader;

        public DataFileWithHeaderIterator(File dataFile) {
            try {
                dataFileReader = new BufferedReader(new FileReader(dataFile));
                headers = dataFileReader.readLine().split(",", -1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
