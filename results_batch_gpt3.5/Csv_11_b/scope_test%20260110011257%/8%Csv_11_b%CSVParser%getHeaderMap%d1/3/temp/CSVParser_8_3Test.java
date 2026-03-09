package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import java.io.Reader;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_3Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a real CSVParser instance with mocked Reader and CSVFormat to avoid complex initialization
        Reader mockedReader = mock(Reader.class);
        CSVFormat mockedFormat = mock(CSVFormat.class);

        // Use reflection to create CSVParser instance without calling constructor (to avoid final fields initialization issues)
        parser = (CSVParser) java.lang.reflect.Proxy.newProxyInstance(
                CSVParser.class.getClassLoader(),
                new Class<?>[] { CSVParser.class },
                (proxy, method, args) -> null);

        // Instead, instantiate via constructor and then override final headerMap field with reflection
        parser = new CSVParser(mockedReader, mockedFormat);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_ReturnsNullWhenHeaderMapIsNull() throws Exception {
        // Set headerMap to null via reflection
        setHeaderMap(null);
        assertNull(parser.getHeaderMap());
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderMap_ReturnsCopyOfHeaderMap() throws Exception {
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("header1", 0);
        originalMap.put("header2", 1);
        setHeaderMap(originalMap);

        Map<String, Integer> returnedMap = parser.getHeaderMap();

        assertNotNull(returnedMap);
        assertEquals(originalMap, returnedMap);
        assertNotSame(originalMap, returnedMap);

        // Modifying returned map should not affect original map
        returnedMap.put("header3", 2);
        assertFalse(originalMap.containsKey("header3"));
    }

    private void setHeaderMap(Map<String, Integer> map) throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier from the headerMap field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(parser, map);
    }
}