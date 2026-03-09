package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_2Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Instantiate CSVParser with null Reader and CSVFormat
        csvParser = new CSVParser(null, null);
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapWhenHeaderMapIsNull() throws Exception {
        setFinalField(csvParser, "headerMap", null);

        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapReturnsCopy() throws Exception {
        Map<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("Name", 0);
        originalMap.put("Age", 1);

        setFinalField(csvParser, "headerMap", originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to return non-null map");
        assertEquals(originalMap, result, "Returned map should be equal to original headerMap");
        assertNotSame(originalMap, result, "Returned map should be a new copy, not the same instance");

        // Modify returned map and verify original map is not affected
        result.put("City", 2);
        assertFalse(originalMap.containsKey("City"), "Original headerMap should not be affected by changes to returned map");
    }
}