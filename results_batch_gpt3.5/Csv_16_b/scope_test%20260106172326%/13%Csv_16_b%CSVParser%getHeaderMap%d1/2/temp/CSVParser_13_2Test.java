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
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_2Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);
        // Use the public constructor without the long parameters to avoid issues
        parser = new CSVParser(new java.io.StringReader(""), format);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_whenHeaderMapIsNull() throws Exception {
        setHeaderMap(null);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected getHeaderMap() to return null when headerMap is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_whenHeaderMapIsEmpty() throws Exception {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        setHeaderMap(map);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNotNull(result, "Expected getHeaderMap() to return a non-null map");
        assertTrue(result.isEmpty(), "Expected returned map to be empty");
        assertNotSame(map, result, "Expected returned map to be a new LinkedHashMap instance");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_whenHeaderMapHasEntries() throws Exception {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("header1", 0);
        map.put("header2", 1);
        setHeaderMap(map);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNotNull(result, "Expected getHeaderMap() to return a non-null map");
        assertEquals(2, result.size(), "Expected returned map to contain 2 entries");
        assertEquals(0, result.get("header1"));
        assertEquals(1, result.get("header2"));
        assertNotSame(map, result, "Expected returned map to be a new LinkedHashMap instance");

        result.put("header3", 2);
        @SuppressWarnings("unchecked")
        Map<String, Integer> original = (Map<String, Integer>) getHeaderMapField().get(parser);
        assertFalse(original.containsKey("header3"), "Original headerMap should not be affected by modifications to returned map");
    }

    private void setHeaderMap(Map<String, Integer> map) throws Exception {
        Field headerMapField = getHeaderMapField();
        headerMapField.setAccessible(true);
        removeFinalModifier(headerMapField);
        headerMapField.set(parser, map);
    }

    private Field getHeaderMapField() throws Exception {
        return CSVParser.class.getDeclaredField("headerMap");
    }

    private static void removeFinalModifier(Field field) throws Exception {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }
}