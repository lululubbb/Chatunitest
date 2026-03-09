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

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVParser_8_5Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock CSVFormat as it's a dependency but not focal here
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVParser instance via constructor with a Reader, passing null for simplicity
        csvParser = new CSVParser(null, format);

        // Use reflection to set the private final headerMap field
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Prepare a LinkedHashMap with sample header entries
        LinkedHashMap<String, Integer> headerMap = new LinkedHashMap<>();
        headerMap.put("Name", 0);
        headerMap.put("Age", 1);
        headerMap.put("City", 2);

        // Remove final modifier from the field (only if necessary)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ does not have 'modifiers' field, ignore
        }

        // Set the headerMap field to the sample map
        headerMapField.set(csvParser, headerMap);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapReturnsCopy() {
        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        // Verify returned map equals the original headerMap content
        assertEquals(3, returnedMap.size());
        assertEquals(Integer.valueOf(0), returnedMap.get("Name"));
        assertEquals(Integer.valueOf(1), returnedMap.get("Age"));
        assertEquals(Integer.valueOf(2), returnedMap.get("City"));

        // Verify returned map is a different instance (defensive copy)
        assertNotSame(getHeaderMapField(csvParser), returnedMap);

        // Modify returned map and verify original map is unaffected
        returnedMap.put("Country", 3);
        assertFalse(getHeaderMapField(csvParser).containsKey("Country"));
    }

    // Helper method to access private headerMap field for assertions
    private Map<String, Integer> getHeaderMapField(CSVParser parser) {
        try {
            Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
            headerMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Integer> map = (Map<String, Integer>) headerMapField.get(parser);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}