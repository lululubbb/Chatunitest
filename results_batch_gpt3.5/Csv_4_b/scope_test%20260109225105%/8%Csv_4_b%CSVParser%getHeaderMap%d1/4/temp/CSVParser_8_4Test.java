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
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_4Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a CSVParser instance with dummy parameters (mock Reader and CSVFormat)
        csvParser = new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class));

        // Use reflection to set the final headerMap field to a mutable LinkedHashMap to allow modification in tests
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier via reflection if needed (not always necessary)
        // But typically, setting accessible true is enough to set final fields in tests
        headerMapField.set(csvParser, new LinkedHashMap<String, Integer>());
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapReturnsCopy() throws Exception {
        // Use reflection to set the private headerMap field
        Map<String, Integer> originalHeaderMap = new LinkedHashMap<>();
        originalHeaderMap.put("header1", 0);
        originalHeaderMap.put("header2", 1);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        headerMapField.set(csvParser, originalHeaderMap);

        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        // Confirm returned map equals original map
        assertEquals(originalHeaderMap, returnedMap);

        // Confirm returned map is a new instance (not the same reference)
        assertNotSame(originalHeaderMap, returnedMap);

        // Modify returned map should not affect original map
        returnedMap.put("header3", 2);
        assertFalse(originalHeaderMap.containsKey("header3"));
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMapWhenHeaderMapIsEmpty() throws Exception {
        // Set headerMap to empty LinkedHashMap
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        LinkedHashMap<String, Integer> emptyMap = new LinkedHashMap<>();
        headerMapField.set(csvParser, emptyMap);

        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        assertNotNull(returnedMap);
        assertTrue(returnedMap.isEmpty());
        assertNotSame(emptyMap, returnedMap);
    }

}