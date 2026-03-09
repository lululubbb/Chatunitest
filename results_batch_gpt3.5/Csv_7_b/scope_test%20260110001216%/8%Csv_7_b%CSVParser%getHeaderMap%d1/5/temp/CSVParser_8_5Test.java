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

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_8_5Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVParser instance with dummy CSVFormat and Reader
        parser = new CSVParser(new java.io.StringReader("header1,header2\nvalue1,value2"), CSVFormat.DEFAULT);

        // Use reflection to set the private final headerMap field to null for one test
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier on headerMap field (works on Java 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(parser, null);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMap_ReturnsNull_WhenHeaderMapIsNull() throws Exception {
        // headerMap is null from setUp
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMap_ReturnsCopyOfHeaderMap_WhenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap to set as headerMap
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("A", 0);
        originalMap.put("B", 1);

        // Set the private final headerMap field to our map using reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier on headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(parser, originalMap);

        Map<String, Integer> returnedMap = parser.getHeaderMap();

        assertNotNull(returnedMap);
        assertEquals(2, returnedMap.size());
        assertEquals(0, returnedMap.get("A"));
        assertEquals(1, returnedMap.get("B"));

        // Verify returned map is a new instance (not the same reference)
        assertNotSame(originalMap, returnedMap);

        // Modify returned map should not affect original map
        returnedMap.put("C", 2);
        assertFalse(originalMap.containsKey("C"));
    }
}