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

public class CSVParser_13_6Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy CSVFormat mock or real instance as needed
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVParser instance with Reader and CSVFormat constructor
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Set headerMap field to null for first test
        setFinalField(parser, "headerMap", null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapWhenHeaderMapIsNull() {
        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap() to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapReturnsCopy() throws Exception {
        // Prepare a LinkedHashMap with some headers
        LinkedHashMap<String, Integer> originalHeaderMap = new LinkedHashMap<>();
        originalHeaderMap.put("Name", 0);
        originalHeaderMap.put("Age", 1);

        // Set headerMap field to originalHeaderMap
        setFinalField(parser, "headerMap", originalHeaderMap);

        Map<String, Integer> returnedMap = parser.getHeaderMap();

        // Check returned map equals original map
        assertNotNull(returnedMap);
        assertEquals(originalHeaderMap, returnedMap);

        // Check returned map is a different instance (defensive copy)
        assertNotSame(originalHeaderMap, returnedMap);

        // Modifying returned map should not affect original headerMap
        returnedMap.put("NewHeader", 2);
        assertFalse(originalHeaderMap.containsKey("NewHeader"));
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}