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
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_3Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare a dummy CSVFormat mock or real instance as needed
        CSVFormat format = mock(CSVFormat.class);

        // Create a CSVParser instance with null Reader and mocked format (constructor is public)
        parser = new CSVParser(null, format);

        // Use reflection to set the final headerMap field to null initially for tests
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier to allow setting the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // Set headerMap field to null via reflection (final field, but setting is allowed for test purposes)
        headerMapField.set(parser, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_whenHeaderMapIsNull() throws Exception {
        // Set headerMap field to null via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier to allow setting the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(parser, null);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_whenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap with some entries
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        // Set headerMap field to the map via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier to allow setting the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(parser, originalMap);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNotNull(result, "Expected non-null map when headerMap is set");
        assertEquals(originalMap, result, "Expected returned map to equal the original headerMap");
        assertNotSame(originalMap, result, "Expected returned map to be a new instance, not the original");

        // Verify that modifying the returned map does not affect the original headerMap
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original headerMap should not be affected by changes to returned map");
    }
}