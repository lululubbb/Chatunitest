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

class CSVParser_8_6Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);
        Reader reader = mock(Reader.class);

        csvParser = new CSVParser(reader, format);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapReturnsNullWhenHeaderMapIsNull() throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier on headerMap field (works on Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, null);

        Map<String, Integer> result = csvParser.getHeaderMap();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapReturnsCopyOfHeaderMap() throws Exception {
        LinkedHashMap<String, Integer> originalHeaderMap = new LinkedHashMap<>();
        originalHeaderMap.put("A", 1);
        originalHeaderMap.put("B", 2);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerMapField.set(csvParser, originalHeaderMap);

        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        assertNotNull(returnedMap);
        assertEquals(originalHeaderMap, returnedMap);
        assertNotSame(originalHeaderMap, returnedMap);

        returnedMap.put("C", 3);
        assertFalse(originalHeaderMap.containsKey("C"));
    }
}