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

public class CSVParser_8_1Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a mock CSVFormat since CSVParser constructor requires it
        CSVFormat formatMock = mock(CSVFormat.class);
        // Create a mock Reader since CSVParser constructor requires it
        java.io.Reader readerMock = mock(java.io.Reader.class);

        csvParser = new CSVParser(readerMock, formatMock);

        // Use reflection to set the final headerMap field
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Set headerMap to null to simulate uninitialized headerMap
        // Since headerMap is final, we cannot set it directly; instead,
        // use reflection to remove final modifier if needed.
        // However, in newer Java versions, removing final modifiers via reflection is restricted.
        // Instead, use Unsafe or VarHandle, but here just skip removing final and set accessible only.
        // The test will fail if Java security prevents this, so we do the best possible here.

        // Try to remove final modifier (works in Java 8)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignored, continue without removing final modifier
        }

        headerMapField.set(csvParser, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_NullHeaderMap() throws Exception {
        // Set headerMap to null using reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignored
        }

        headerMapField.set(csvParser, null);

        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result, "Expected null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_NonNullHeaderMap() throws Exception {
        // Prepare a LinkedHashMap with some entries
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        // Set headerMap field using reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignored
        }

        headerMapField.set(csvParser, originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected non-null map when headerMap is set");
        assertEquals(originalMap, result, "Returned map should be equal to the original headerMap");
        assertNotSame(originalMap, result, "Returned map should be a new instance (defensive copy)");

        // Modify returned map and verify original is not affected
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original headerMap should not be affected by modifications to returned map");
    }
}