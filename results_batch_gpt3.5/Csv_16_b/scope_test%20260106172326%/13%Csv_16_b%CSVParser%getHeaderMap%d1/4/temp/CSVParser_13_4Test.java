package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_13_4Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy CSVFormat mock or real instance as needed
        CSVFormat format = mock(CSVFormat.class);

        // Create a CSVParser instance with a Reader and format (using the public constructor)
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set the private final headerMap field to null for one test
        setFinalField(parser, "headerMap", null);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapReturnsNullWhenHeaderMapIsNull() throws Exception {
        // headerMap is already set to null in setUp
        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapReturnsCopyOfHeaderMap() throws Exception {
        // Prepare a LinkedHashMap to set as headerMap
        LinkedHashMap<String, Integer> originalHeaderMap = new LinkedHashMap<>();
        originalHeaderMap.put("Name", 0);
        originalHeaderMap.put("Age", 1);

        // Set the private final headerMap field via reflection
        setFinalField(parser, "headerMap", originalHeaderMap);

        Map<String, Integer> result = parser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to not return null when headerMap is set");
        assertEquals(originalHeaderMap, result, "Expected getHeaderMap to return a map equal to headerMap");
        assertNotSame(originalHeaderMap, result, "Expected getHeaderMap to return a new LinkedHashMap instance");

        // Modify result and verify originalHeaderMap is not affected (defensive copy)
        result.put("NewColumn", 2);
        assertFalse(originalHeaderMap.containsKey("NewColumn"), "Original headerMap should not be modified");
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field (works in Java 8 and earlier)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}