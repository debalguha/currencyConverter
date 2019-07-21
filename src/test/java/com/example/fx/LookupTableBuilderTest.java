package com.example.fx;


import com.example.fx.conversion.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class LookupTableBuilderTest {
    @Test
    public void test_buildFXConversionTable() throws Exception{
        String filePath = Thread.currentThread().getContextClassLoader().getResource("ref_badcurr.csv").getFile();
        Map<Pair<String, String>, Converter> pairConverterMap = LookupTableBuilderV2.withRefDataSource(new FileFXRefDataSource(new File(filePath))).buildFXConversionTable();
        Assert.assertNotNull(pairConverterMap);
        Assert.assertFalse(pairConverterMap.isEmpty());
    }

}
