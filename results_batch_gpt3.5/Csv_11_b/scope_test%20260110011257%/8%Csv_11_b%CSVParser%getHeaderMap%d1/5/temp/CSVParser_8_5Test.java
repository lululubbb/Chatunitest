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
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_5Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a CSVFormat mock as needed
        CSVFormat format = mock(CSVFormat.class);

        // Create a dummy Reader as it is required by the constructor
        java.io.Reader reader = new java.io.StringReader("");

        // Create CSVParser instance normally
        csvParser = new CSVParser(reader, format);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNull() throws Exception {
        // Set headerMap field to null via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, null);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNull(result, "Expected null when headerMap field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap for headerMap
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        // Set headerMap field to originalMap via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected non-null map when headerMap field is set");
        assertEquals(originalMap.size(), result.size(), "Result map size should match original");
        assertEquals(originalMap, result, "Result map should be equal to original map");
        assertNotSame(originalMap, result, "Result map should be a new LinkedHashMap instance");

        // Verify that modifying result does not affect originalMap
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original map should not be affected by changes to result");
    }
}