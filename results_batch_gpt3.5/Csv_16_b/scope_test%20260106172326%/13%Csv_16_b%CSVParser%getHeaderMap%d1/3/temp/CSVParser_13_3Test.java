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
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_3Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a dummy CSVFormat mock or instance as needed
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVParser instance with a Reader and CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), format, 0L, 0L);

        // Use reflection to set the headerMap field to null
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNull() {
        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap to return null when headerMap field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_WhenHeaderMapIsNotNull() throws Exception {
        // Prepare a LinkedHashMap to assign to headerMap field
        LinkedHashMap<String, Integer> internalMap = new LinkedHashMap<>();
        internalMap.put("header1", 1);
        internalMap.put("header2", 2);

        // Set the private headerMap field to the prepared map
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, internalMap);

        // Invoke getHeaderMap and verify it returns a copy, not the original
        Map<String, Integer> result = parser.getHeaderMap();

        assertNotNull(result, "Expected getHeaderMap to return non-null map when headerMap field is set");
        assertEquals(internalMap.size(), result.size(), "Returned map size should match internal map size");
        assertEquals(internalMap, result, "Returned map content should be equal to internal map content");
        assertNotSame(internalMap, result, "Returned map should be a new LinkedHashMap copy, not the same instance");

        // Modify the returned map and verify internal map is unaffected
        result.put("header3", 3);
        assertFalse(internalMap.containsKey("header3"), "Modifying returned map should not affect internal headerMap");
    }
}