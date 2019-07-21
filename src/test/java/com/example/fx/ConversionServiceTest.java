package com.example.fx;

import com.example.fx.conversion.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ConversionServiceTest {

    ConversionService service;
    @Before
    public void setup() throws Exception{
        String filePath = Thread.currentThread().getContextClassLoader().getResource("ref_badcurr_good.csv").getFile();
        service = new ConversionServiceByLookup(LookupTableBuilderV2.withRefDataSource(new FileDataSource(new File(filePath), Utils.lineToFXEntriesMapping())).buildFXConversionTable());
    }
    @Test(expected = RuntimeException.class)
    public void test_BadCurrToBadCurr4Conversion() {
        service.doConversion("BadCurr", "BadCurr4", 20);
    }
    @Test
    public void test_BadCurrToBadCurr3Conversion() {
        double convertedvalue = service.doConversion("BadCurr", "BadCurr3", 20);
        Assert.assertEquals(160, Double.valueOf(convertedvalue).intValue());
    }
    @Test
    public void test_BadCurrToBadCurr3ConversionWithUpdateEvent() {
        //Assert.assertEquals(80, Double.valueOf(service.doConversion("BadCurr", "BadCurr3", 20)).intValue());
        ((FXEventListener)service).entryUpdated(createEntryUpdateEvent("BadCurr1", "BadCurr2", 3));
        Assert.assertEquals(240, Double.valueOf(service.doConversion("BadCurr", "BadCurr3", 20)).intValue());
    }
    FXEvent createEntryUpdateEvent(String fromCurr, String toCurr, double updatedFactor){
        return new FXEvent("TEST", new FXEntry(fromCurr, toCurr, String.valueOf(updatedFactor)), FXEvent.EventType.ENTRY_UPDATE);
    }
}
