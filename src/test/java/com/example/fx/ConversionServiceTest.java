package com.example.fx;

import com.example.fx.conversion.ConversionService;
import com.example.fx.conversion.ConversionServiceByLookup;
import com.example.fx.conversion.FXEvent;
import com.example.fx.conversion.FXEventListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class ConversionServiceTest {

    ConversionService service;
    @Before
    public void setup() {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("ref_badcurr_good_shrink.csv").getFile();
        service = new ConversionServiceByLookup(LookupTableBuilderV2.withRefDataSource(new FileFXRefDataSource(new File(filePath))).buildFXConversionTable());
    }
    @Test
    public void test_BadCurrToBadCurr3Conversion() {
        double convertedvalue = service.doConversion("BadCurr", "BadCurr3", 20);
        Assert.assertEquals(160, Double.valueOf(convertedvalue).intValue());
    }
    @Test
    //@Ignore
    public void test_BadCurrToBadCurr3ConversionWithUpdateEvent() {
        //Assert.assertEquals(80, Double.valueOf(service.doConversion("BadCurr", "BadCurr3", 20)).intValue());
        ((FXEventListener)service).entryUpdated(createEntryUpdateEvent("BadCurr1", "BadCurr2", 3));
        Assert.assertEquals(240, Double.valueOf(service.doConversion("BadCurr", "BadCurr3", 20)).intValue());
    }
    FXEvent createEntryUpdateEvent(String fromCurr, String toCurr, double updatedFactor){
        return new FXEvent("TEST", new FXEntry(fromCurr, toCurr, String.valueOf(updatedFactor)), FXEvent.EventType.ENTRY_UPDATE);
    }
}
