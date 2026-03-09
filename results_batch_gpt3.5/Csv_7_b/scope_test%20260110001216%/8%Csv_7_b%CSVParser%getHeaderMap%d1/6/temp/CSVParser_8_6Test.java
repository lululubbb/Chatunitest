package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_6Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Using the public constructor with a Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), mock(CSVFormat.class));

        // Use reflection to set the final headerMap field to a mutable LinkedHashMap (initially empty)
        setFinalField(csvParser, "headerMap", new LinkedHashMap<String, Integer>());
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_NullHeaderMap() throws Exception {
        // Set headerMap field to null using reflection
        setFinalField(csvParser, "headerMap", null);

        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result, "Expected null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_NonNullHeaderMap() throws Exception {
        // Prepare a LinkedHashMap for headerMap field
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("A", 0);
        originalMap.put("B", 1);

        // Set headerMap field to originalMap using reflection
        setFinalField(csvParser, "headerMap", originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected non-null map when headerMap is set");
        assertEquals(originalMap, result, "Expected returned map to equal original headerMap");
        assertNotSame(originalMap, result, "Expected returned map to be a new LinkedHashMap instance");

        // Verify that modifying the returned map does not affect the original headerMap
        result.put("C", 2);
        assertFalse(originalMap.containsKey("C"), "Original headerMap should not be affected by changes to returned map");
    }

    private static void setFinalField(Object targetObject, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(targetObject, value);
    }
}