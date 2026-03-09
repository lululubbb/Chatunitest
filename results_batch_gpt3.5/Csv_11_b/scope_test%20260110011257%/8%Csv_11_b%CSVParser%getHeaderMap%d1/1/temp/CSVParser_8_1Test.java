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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_1Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to create a real instance bypassing constructor
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        // Pass a non-null CSVFormat instance to avoid NullPointerException in constructor
        csvParser = constructor.newInstance(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Set the final headerMap field to null via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field to allow modification
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNull() throws Exception {
        // headerMap is already set to null in setUp
        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap to return null when headerMap field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap for headerMap
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        // Set headerMap field to originalMap
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field to allow modification
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to return a non-null map when headerMap field is set");
        assertEquals(originalMap, result, "Returned map should be equal to the original headerMap content");
        assertNotSame(originalMap, result, "Returned map should be a new LinkedHashMap instance (a copy)");

        // Modifying returned map should not affect original headerMap
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original headerMap should not be affected by changes to returned map");
    }
}