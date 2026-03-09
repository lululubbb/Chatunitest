package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_4_6Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = CSVFormat.DEFAULT.withHeader("A", "B", "C");
    }

    @AfterEach
    void tearDown() {
        format = null;
    }

    @Test
    @Timeout(8000)
    void testConstructorWithValidReaderAndFormat() throws IOException {
        String csvContent = "A,B,C\n1,2,3\n4,5,6";
        Reader reader = new StringReader(csvContent);

        try (CSVParser parser = new CSVParser(reader, format)) {
            assertNotNull(parser);

            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertNotNull(headerMap);
            assertEquals(3, headerMap.size());
            assertEquals(0, headerMap.get("A"));
            assertEquals(1, headerMap.get("B"));
            assertEquals(2, headerMap.get("C"));

            assertEquals(0, parser.getRecordNumber());
            assertFalse(parser.isClosed());
        }
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullReaderThrows() {
        assertThrows(NullPointerException.class, () -> new CSVParser(null, format));
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullFormatThrows() throws IOException {
        String csvContent = "A,B,C\n1,2,3\n";
        Reader reader = new StringReader(csvContent);
        assertThrows(NullPointerException.class, () -> new CSVParser(reader, null));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeaderReflection() throws Exception {
        String csvContent = "X,Y,Z\n7,8,9";
        Reader reader = new StringReader(csvContent);

        try (CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(parser);

            assertNotNull(headerMap);
            assertEquals(3, headerMap.size());
            assertTrue(headerMap.containsKey("X"));
            assertTrue(headerMap.containsKey("Y"));
            assertTrue(headerMap.containsKey("Z"));
        }
    }
}