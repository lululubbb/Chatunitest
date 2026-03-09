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

class CSVParser_8_3Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVFormat mock or dummy, since constructor requires it
        CSVFormat format = mock(CSVFormat.class);

        // Use a dummy Reader (not used in this test)
        csvParser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set the private final headerMap field
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Prepare a LinkedHashMap with some entries
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("header1", 0);
        map.put("header2", 1);

        // Remove final modifier from headerMap field if needed (Java 12+ does not allow this easily)
        // The following is a workaround for Java 8 and below:
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // If running on Java 12+, skip removing final modifier
        }

        // Set the headerMap field to our map
        headerMapField.set(csvParser, map);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapReturnsCopy() throws Exception {
        Map<String, Integer> returnedMap = csvParser.getHeaderMap();

        // The returned map should not be the same instance as the internal headerMap
        assertNotSame(getInternalHeaderMap(csvParser), returnedMap);

        // The returned map should be equal in content
        assertEquals(getInternalHeaderMap(csvParser), returnedMap);

        // Modifying returned map should not affect internal headerMap
        returnedMap.put("header3", 2);
        assertFalse(getInternalHeaderMap(csvParser).containsKey("header3"));
    }

    private Map<String, Integer> getInternalHeaderMap(CSVParser parser) throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> map = (Map<String, Integer>) headerMapField.get(parser);
        return map;
    }
}