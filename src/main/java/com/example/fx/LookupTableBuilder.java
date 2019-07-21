package com.example.fx;

import com.example.fx.conversion.Converter;
import com.example.fx.conversion.ConverterImpl;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LookupTableBuilder {

    protected FXRefDataSource refDataSource;
    protected static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    public static LookupTableBuilder withRefDataSource(FXRefDataSource refDataSource) {
        LookupTableBuilder lookupTableBuilder = new LookupTableBuilder();
        lookupTableBuilder.refDataSource = refDataSource;
        return lookupTableBuilder;
    }

    /*public Map<Pair<String, String>, Converter> buildFXConversionTable() {
        Queue<FXEntry> ballParkedEntries = new LinkedList<>();
        Map<Pair<String, String>, Converter> directMappingTable = refDataSource.getFxValueEntries()
                .<Optional<ConverterImpl>>map(fxEntry -> {
                    if(fxEntry.getConversionEntry() != null && !fxEntry.getConversionEntry().trim().isEmpty()){
                        return Optional.of(new ConverterImpl(fxEntry));
                    } else if (canTransformTODouble(fxEntry.getConversionEntry())) {
                        return Optional.of(new ConverterImpl(new Pair<>(fxEntry.getFromCurrency(), fxEntry.getToCurrency()),
                                buildFunction(Double.parseDouble(fxEntry.getConversionEntry()))));
                    } else {
                        ballParkedEntries.offer(fxEntry);
                        return Optional.empty();
                    }
                })
                .filter(optionalVal -> optionalVal.isPresent())
                .map(optionalVal -> optionalVal.get())
                .collect(Collectors.toMap(Converter::getPair, Function.identity()));
        //directMappingTable.putAll(lookupReferenceTableMapping(ballParkedEntries, directMappingTable));
        directMappingTable.putAll(buildReferenceTableMapping(ballParkedEntries, directMappingTable));
        return directMappingTable;
    }*/

    protected Map<Pair<String, String>, Converter> lookupReferenceTableMapping(Queue<FXEntry> ballParkedEntries, Map<Pair<String, String>, Converter> directMappingTable) {
        return ballParkedEntries.stream().map(currentEntry -> {
            Pair<String, String> currentPair = Pair.fromFXEntry(currentEntry);
            Converter lookedUpConverter = directMappingTable.get(new Pair<>(currentEntry.getFromCurrency(), currentEntry.getConversionEntry()));
            if (lookedUpConverter != null) {
                FXEntry referencedEntry = new FXEntry(lookedUpConverter.getPair()._2(), currentEntry.getToCurrency(), null);
                Converter referencedConverter = directMappingTable.get(Pair.fromFXEntry(referencedEntry));
                if (referencedConverter != null) {
                    return buildConverterFromPairAndFunction(currentPair, lookedUpConverter.getConverterFunction().get().andThen(referencedConverter.getConverterFunction().get()));
                }
            }
            return new ConverterImpl(currentEntry);
        }).collect(Collectors.toMap(Converter::getPair, Function.identity()));
    }


    protected Converter buildConverterFromPairAndFunction(Pair<String, String> aPair, Function<Double, Double> function) {
        return new ConverterImpl(aPair, function);
    }

    protected Map<Pair<String, String>, Converter> buildReferenceTableMapping(Queue<FXEntry> ballParkedEntries, Map<Pair<String, String>, Converter> directMappingTable) {
        Map<Pair<String, String>, Converter> referenceTableMapping = new HashMap<>();
        Set<FXEntry> unresolvedEntries = new HashSet<>();
        while(!ballParkedEntries.isEmpty()){
            FXEntry currentEntry = ballParkedEntries.remove();
            Pair currentPair = Pair.fromFXEntry(currentEntry);
            Converter lookedUpConverter = lookupConverter(new Pair<>(currentEntry.getFromCurrency(), currentEntry.getConversionEntry()), directMappingTable, referenceTableMapping);
            if(Objects.nonNull(lookedUpConverter) && lookedUpConverter.getConverterFunction().isPresent()){
                FXEntry referencedEntry = new FXEntry(lookedUpConverter.getPair()._2(), currentEntry.getToCurrency(), null);
                Optional<Function<Double, Double>> composedFunction = chainAndComposeFunction(Pair.fromFXEntry(referencedEntry), directMappingTable, referenceTableMapping, lookedUpConverter.getConverterFunction().get());
                if(composedFunction.isPresent()){
                    referenceTableMapping.put(currentPair, new ConverterImpl(currentPair, composedFunction.get()));
                } else {
                    if(ballParkedEntries.contains(referencedEntry)){
                        // A reference is yet to be resolved.
                        // Add back this entry to the end of the queue
                        ballParkedEntries.offer(currentEntry);
                    } else {
                        // The reference entry is not present in any table.
                        // The reference entry does not exist in ballparked entries.
                        // It is unlikely to be existing at all!! Raise it!!
                        //referenceTableMapping.put(Pair.fromFXEntry(currentEntry), new ConverterImpl(currentEntry));
                        unresolvedEntries.add(currentEntry);
                    }
                }
            } else {
                unresolvedEntries.add(currentEntry);
            }
        }
        //processUnResolvedEntries(unresolvedEntries, Collections.unmodifiableMap(directMappingTable), Collections.unmodifiableMap(referenceTableMapping));
        return referenceTableMapping;
    }

   /* protected void processUnResolvedEntries(Set<FXEntry> unresolvedEntries, Map<Pair<String, String>, Converter> directMappingTable, Map<Pair<String, String>, Converter> referenceMappingTable) {
        unresolvedEntries.forEach(anEntry -> {
            processSingleEntry(anEntry, directMappingTable, referenceMappingTable, new LinkedList<>());
        });
    }

    protected void processSingleEntry(FXEntry anEntry, Map<Pair<String, String>, Converter> directMappingTable, Map<Pair<String, String>, Converter> referenceMappingTable, LinkedList<String> mappingTrace) {
        if(anEntry == null || mappingTrace.contains(anEntry.getFromCurrency())){
            return;
        }
        mappingTrace.add(anEntry.fromCurrency);
        //proce
    }*/

    protected Converter lookupConverter(Pair<String, String> pairToLookup, Map<Pair<String, String>, Converter> directMappingTable, Map<Pair<String, String>, Converter> referenceTableMapping) {
        return directMappingTable.containsKey(pairToLookup) ? directMappingTable.get(pairToLookup) : referenceTableMapping.get(pairToLookup);
    }

    protected Optional<Function<Double, Double>> chainAndComposeFunction(Pair<String, String> pairToFindConverter, Map<Pair<String, String>, Converter> directMappingTable, Map<Pair<String, String>, Converter> referenceTableMapping, Function<Double, Double> functionToComposeWith) {
        Converter converter = lookupConverter(pairToFindConverter, directMappingTable, referenceTableMapping);
        if(Objects.nonNull(converter) && converter.getConverterFunction().isPresent()){
            return Optional.of(functionToComposeWith.andThen(converter.getConverterFunction().get()));
        }
        /*if(directMappingTable.containsKey(pairToFindConverter)){
            return Optional.of(functionToComposeWith.andThen(directMappingTable.get(pairToFindConverter).getConverterFunction().get()));
        } else if(referenceTableMapping.containsKey(pairToFindConverter)){
            if(referenceTableMapping.get(pairToFindConverter).getConverterFunction().isPresent()) {
                return Optional.of(functionToComposeWith.andThen(referenceTableMapping.get(pairToFindConverter).getConverterFunction().get()));
            }
        }*/

        return Optional.empty();
    }

}
