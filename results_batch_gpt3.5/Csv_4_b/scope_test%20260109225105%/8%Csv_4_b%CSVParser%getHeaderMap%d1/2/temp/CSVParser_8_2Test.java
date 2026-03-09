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

public class CSVParser_8_2Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create instance with a dummy Reader and CSVFormat (mocked)
        csvParser = new CSVParser(new java.io.StringReader(""), mock(CSVFormat.class));

        // Use reflection to set the private final headerMap field
        Map<String, Integer> headerMap = new LinkedHashMap<>();
        headerMap.put("header1", 0);
        headerMap.put("header2", 1);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier on headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, headerMap);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_ReturnsCopyOfHeaderMap() {
        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("header1"));
        assertEquals(Integer.valueOf(1), result.get("header2"));

        // Modify returned map and verify original map is not affected
        result.put("header3", 2);
        Map<String, Integer> originalHeaderMap = getHeaderMapViaReflection(csvParser);
        assertFalse(originalHeaderMap.containsKey("header3"));
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_EmptyHeaderMap() throws Exception {
        // Set headerMap to empty map
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier on headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~Modifier.FINAL);

        headerMapField.set(csvParser, new LinkedHashMap<>());

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Helper method to get private headerMap field via reflection
    private Map<String, Integer> getHeaderMapViaReflection(CSVParser parser) {
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