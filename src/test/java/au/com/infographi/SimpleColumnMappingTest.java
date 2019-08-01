package au.com.infographi;

import au.com.infographi.sc.rules.SimpleColumnMapping;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleColumnMappingTest {
    @Test
    public void test_replaceSpaceFunction() {
        assertEquals("BenifitCategory", SimpleColumnMapping.replaceSpaceFunction().apply("Benifit Category"));
    }
}
