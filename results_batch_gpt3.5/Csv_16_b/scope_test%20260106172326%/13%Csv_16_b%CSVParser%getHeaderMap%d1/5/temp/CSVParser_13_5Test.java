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

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_5Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Use the parse factory method to create CSVParser with headerMap initialized
        csvParser = CSVParser.parse("", CSVFormat.DEFAULT);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNull() throws Exception {
        // Set headerMap field to null via reflection
        setHeaderMap(null);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNull(result, "Expected getHeaderMap to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap with sample data
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);

        setHeaderMap(originalMap);

        Map<String, Integer> result = csvParser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to return a non-null map when headerMap is set");
        assertEquals(originalMap, result, "Expected returned map to be equal to the original headerMap");
        assertNotSame(originalMap, result, "Expected returned map to be a new LinkedHashMap instance");

        // Modify returned map and verify original map is unaffected
        result.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"), "Original headerMap should not be affected by changes to returned map");
    }

    // Helper method to set private final field headerMap via reflection
    private void setHeaderMap(Map<String, Integer> map) throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, map);
    }
}