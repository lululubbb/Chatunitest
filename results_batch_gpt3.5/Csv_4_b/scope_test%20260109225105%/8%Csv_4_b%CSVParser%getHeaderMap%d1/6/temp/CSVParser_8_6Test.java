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

public class CSVParser_8_6Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy CSVFormat mock since it's final and no details provided
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVParser instance with a Reader and format
        // Using StringReader with empty string since constructor requires Reader
        csvParser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set the private final headerMap field
        Map<String, Integer> headerMap = new LinkedHashMap<>();
        headerMap.put("header1", 0);
        headerMap.put("header2", 1);
        setPrivateFinalField(csvParser, "headerMap", headerMap);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_ReturnsCopyOfHeaderMap() throws Exception {
        Map<String, Integer> headerMapCopy = csvParser.getHeaderMap();

        assertNotNull(headerMapCopy, "Header map copy should not be null");
        assertEquals(2, headerMapCopy.size(), "Header map copy should have 2 entries");
        assertEquals(Integer.valueOf(0), headerMapCopy.get("header1"));
        assertEquals(Integer.valueOf(1), headerMapCopy.get("header2"));

        // Mutate the returned map and verify original headerMap in csvParser is not affected
        headerMapCopy.put("header3", 2);
        Map<String, Integer> originalHeaderMap = getPrivateField(csvParser, "headerMap");
        assertFalse(originalHeaderMap.containsKey("header3"), "Original headerMap should not contain new key");
    }

    // Helper method to set private final field via reflection
    private void setPrivateFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present (works on Java 8 and earlier)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    // Helper method to get private field via reflection
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object target, String fieldName) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }
}