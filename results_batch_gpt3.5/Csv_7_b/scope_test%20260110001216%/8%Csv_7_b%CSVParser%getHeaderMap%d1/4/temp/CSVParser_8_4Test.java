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
import java.util.Collection;
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

class CSVParser_8_4Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = mock(CSVFormat.class);

        // Use public constructor with empty reader and mocked format
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set the final 'headerMap' field to a non-null default map to avoid NPE in getHeaderMap()
        setFinalField(parser, "headerMap", new LinkedHashMap<String, Integer>());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMap_whenHeaderMapIsNull() throws Exception {
        setFinalField(parser, "headerMap", null);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result, "Expected null when headerMap field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMap_whenHeaderMapIsNotNull() throws Exception {
        LinkedHashMap<String, Integer> originalMap = new LinkedHashMap<>();
        originalMap.put("col1", 0);
        originalMap.put("col2", 1);

        setFinalField(parser, "headerMap", originalMap);

        Map<String, Integer> result = parser.getHeaderMap();

        assertNotNull(result, "Expected non-null map when headerMap field is set");
        assertEquals(originalMap.size(), result.size(), "Map size should be equal");
        assertEquals(originalMap, result, "Returned map should be equal to original");
        assertNotSame(originalMap, result, "Returned map should be a new instance");
    }

    // Helper method to set final field via reflection
    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present (Java 12+ may not allow this, but for earlier versions this works)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        field.set(target, value);
    }
}