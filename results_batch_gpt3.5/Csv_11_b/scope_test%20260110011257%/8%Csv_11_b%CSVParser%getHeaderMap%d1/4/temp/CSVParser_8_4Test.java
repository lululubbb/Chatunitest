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

public class CSVParser_8_4Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy CSVFormat (mock or real as needed)
        CSVFormat format = mock(CSVFormat.class);

        // Use a Reader that won't be used since we will set headerMap via reflection
        csvParser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set the final headerMap field to null initially
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from headerMap field to allow setting it
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNull_ReturnsNull() throws Exception {
        // Set headerMap field to null via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, null);

        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNotNull_ReturnsCopy() throws Exception {
        // Prepare a LinkedHashMap with some entries
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        // Set headerMap field to originalMap via reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to return non-null when headerMap is set");
        assertEquals(originalMap, result, "Returned map should equal the original headerMap");
        assertNotSame(originalMap, result, "Returned map should be a new copy, not the same instance");

        // Modify returned map and verify original map is unaffected
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original map should not be modified when returned map is changed");
    }
}