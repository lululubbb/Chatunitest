package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVParser_8_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderMapWhenHeaderMapIsNull() throws Exception {
        // Create CSVParser instance
        CSVParser parser = new CSVParser(new StringReader("a,b,c\n1,2,3"), CSVFormat.DEFAULT);

        // Set headerMap to null using reflection
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Since headerMap is final, remove final modifier if possible (Java 8 and below)
        // In newer Java versions this may not work, but here we omit modifying modifiers to avoid compile errors.
        // Just set accessible and set the field value directly (works if not truly final or reflection allows)

        // Use reflection to remove final modifier if possible
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            // Ignore if not possible
        }

        headerMapField.set(parser, null);

        Map<String, Integer> result = parser.getHeaderMap();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderMapWhenHeaderMapIsNotNull() throws Exception {
        // Create CSVParser instance
        CSVParser parser = new CSVParser(new StringReader("a,b,c\n1,2,3"), CSVFormat.DEFAULT);

        // Set headerMap to a LinkedHashMap with some entries using reflection
        LinkedHashMap<String, Integer> headerMap = new LinkedHashMap<>();
        headerMap.put("col1", 0);
        headerMap.put("col2", 1);
        headerMap.put("col3", 2);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);

        // Remove final modifier if present
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            // Ignore if not possible
        }

        headerMapField.set(parser, headerMap);

        Map<String, Integer> result = parser.getHeaderMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));
        assertEquals(Integer.valueOf(1), result.get("col2"));
        assertEquals(Integer.valueOf(2), result.get("col3"));

        // Verify that returned map is a new instance (defensive copy)
        assertNotSame(headerMap, result);
    }
}