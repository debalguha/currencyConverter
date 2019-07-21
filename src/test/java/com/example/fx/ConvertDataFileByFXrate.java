package com.example.fx;

import com.example.fx.conversion.ConversionService;
import com.example.fx.conversion.ConversionServiceByLookup;
import com.example.fx.conversion.Converter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.example.fx.Utils.isEmptystring;

public class ConvertDataFileByFXrate {
    public static void main(String args[]) throws Exception {
        File readDataFile = new File(Thread.currentThread().getContextClassLoader().getResource("data.csv").getFile());
        File refDataFile = new File(Thread.currentThread().getContextClassLoader().getResource("ref.csv").getFile());
        File writeDataFile = new File("output.csv");
        if(writeDataFile.exists()){
            writeDataFile.delete();
        }
        writeDataFile.createNewFile();

        ConversionService conversionService = new ConversionServiceByLookup(refDataLookupTable(refDataFile), false);
        DataSource<Map<String, String>> dataFileSource = new FileDataSource<>(readDataFile, Utils.lineToMapMapping());
        Stream<String> transformedData = dataFileSource.getData().<Map<String, String>>map(aMap -> {
            String fromCurrency = aMap.get("FromCurrency");
            String toCurrency = aMap.get("ToCurrency");
            String fromAmount = aMap.get("FromAmount");
            if (isEmptystring(fromAmount) || isEmptystring(fromCurrency) || isEmptystring(toCurrency)) {
                return Collections.emptyMap();
            }

            return handleExceptionAndForward(fromCurrency, toCurrency, fromAmount, conversionService);
        }).filter(aMap -> !aMap.isEmpty()).map(convertedMap -> fromMapToString(convertedMap));
        new FileDatSync(writeDataFile, new String []{"FromCurrency", "ToCurrency", "FromAmount", "ToAmount"}).accept(transformedData);
        //System.out.println(transformedData.collect(Collectors.toList()).toString());
    }

    private static Map<String, String> handleExceptionAndForward(String fromCurrency, String toCurrency, String fromAmount, ConversionService conversionService) {
        try {
            return createNewPossibleMappingAfterConversion(fromCurrency, toCurrency, fromAmount, conversionService);
        } catch (RuntimeException e) {
            System.out.println(String.format("Exception raised while processing. Details:: [%s]", e.getMessage()));
            return Collections.emptyMap();
        }
    }

    private static String fromMapToString(Map<String, String> convertedMap) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertedMap.get("FromCurrency")).append(",")
               .append(convertedMap.get("ToCurrency")).append(",")
               .append(convertedMap.get("FromAmount")).append(",")
               .append(convertedMap.get("ToAmount"));
        return builder.toString();
    }

    private static Map<String, String> createNewPossibleMappingAfterConversion(String fromCurrency, String toCurrency, String fromAmount, ConversionService conversionService) {
        return QuickMapBuilder.instance().withKeyAndValue("FromCurrency", fromCurrency)
                .withKeyAndValue("ToCurrency", toCurrency)
                .withKeyAndValue("FromAmount", fromAmount)
                .withKeyAndValue("ToAmount", conversionService.doConversion(fromCurrency, toCurrency, fromAmount)).build();
    }

    private static Map<Pair<String, String>, Converter> refDataLookupTable(File refDataFile) throws IOException{
        return LookupTableBuilderV2.withRefDataSource(
                new FileDataSource(refDataFile, Utils.lineToFXEntriesMapping())
        ).buildFXConversionTable();
    }

}
class QuickMapBuilder {
    Map<String, String> dataMap = new HashMap<>();
    public static QuickMapBuilder instance(){
        return new QuickMapBuilder();
    }
    public QuickMapBuilder withKeyAndValue(String key, String value) {
        dataMap.put(key, value);
        return this;
    }
    public Map<String, String> build() {
        return dataMap;
    }
}
