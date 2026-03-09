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

public class CSVParser_8_1Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a CSVFormat mock or dummy as constructor requires it
        CSVFormat format = mock(CSVFormat.class);

        // Use a dummy Reader since constructor requires one, use StringReader with empty content
        java.io.Reader reader = new java.io.StringReader("");

        // Construct CSVParser instance normally
        csvParser = new CSVParser(reader, format);

        // Use reflection to set the final headerMap field to an empty LinkedHashMap to avoid null issues
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, new LinkedHashMap<String, Integer>());
    }

    private void setHeaderMap(Map<String, Integer> map) throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, map);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapReturnsCopyNotOriginal() throws Exception {
        // Setup headerMap with some entries
        Map<String, Integer> originalHeaderMap = new LinkedHashMap<>();
        originalHeaderMap.put("column1", 0);
        originalHeaderMap.put("column2", 1);

        // Set the private final headerMap field to originalHeaderMap via reflection
        setHeaderMap(originalHeaderMap);

        // Call getHeaderMap
        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        // Assert returned map equals the original content
        assertEquals(originalHeaderMap, returnedMap);

        // Assert returned map is a different instance (defensive copy)
        assertNotSame(originalHeaderMap, returnedMap);

        // Modify returned map, original should not change
        returnedMap.put("column3", 2);
        assertFalse(originalHeaderMap.containsKey("column3"));
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapWhenEmpty() throws Exception {
        // Set headerMap to empty map
        Map<String, Integer> emptyHeaderMap = new LinkedHashMap<>();
        setHeaderMap(emptyHeaderMap);

        // Call getHeaderMap
        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        // Assert returned map is empty but not null
        assertNotNull(returnedMap);
        assertTrue(returnedMap.isEmpty());
        assertNotSame(emptyHeaderMap, returnedMap);
    }

}