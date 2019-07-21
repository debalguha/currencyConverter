package com.example.fx;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class FileFXRefDataSourceTest {
    @Test
    public void testIterationOnFile() {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("ref.csv").getFile();
        Iterator<Collection<FXEntry>> iterator = new FileFXRefDataSource(new File(filePath)).iterator();
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
    }
}
