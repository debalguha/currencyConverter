package com.example.fx.conversion;

import com.example.fx.FXEntry;
import com.example.fx.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.fx.Utils.*;

public class FXEventHandlerDelegate {

    public Optional<Map.Entry<Pair<String, String>, Converter>> handleEvent(FXEvent fxEvent, Map<Pair<String, String>, Converter> conversiontable) {
        switch (fxEvent.getEventType()) {
            case ENTRY_UPDATE: return updateConversionTable(conversiontable, fxEvent.getNewEntry());
            default: return Optional.empty();
        }
    }

    private Optional<Map.Entry<Pair<String, String>, Converter>> updateConversionTable(Map<Pair<String, String>, Converter> conversiontable, FXEntry fxEntry) {
        Pair<String, String> entryPair = Pair.fromFXEntry(fxEntry);
        if(canTransformToDouble(fxEntry.getConversionEntry())){
            return Optional.of(new HashMap.SimpleEntry(entryPair, new ConverterImpl(entryPair, buildFunction(Double.parseDouble(fxEntry.getConversionEntry())))));
        } else {
            final Pair<String, String>[] referencedPair = toRefPairs(fxEntry);
            Converter converterRef0 = conversiontable.get(referencedPair[0]);
            Converter converterRef1 = conversiontable.get(referencedPair[1]);
            if(converterRef0 == null && !converterRef0.getConverterFunction().isPresent()
                    && converterRef1 == null && !converterRef1.getConverterFunction().isPresent()) {
                return Optional.of(new HashMap.SimpleEntry(entryPair, new ConverterImpl(entryPair, converterRef0.getPair(), converterRef1.getPair())));
            }
        }
        return Optional.empty();
    }
}
