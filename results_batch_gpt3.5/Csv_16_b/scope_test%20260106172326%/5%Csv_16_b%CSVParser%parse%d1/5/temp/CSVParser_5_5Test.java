package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    @Test
    @Timeout(8000)
    void parse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(csvData, format);

        assertNotNull(parser);
        assertSame(format, getField(parser, "format"));
        assertFalse(parser.isClosed());
        assertEquals(0L, parser.getRecordNumber());
        assertNotNull(parser.getHeaderMap());
        assertTrue(parser.getHeaderMap().containsKey("header1"));
        assertTrue(parser.getHeaderMap().containsKey("header2"));
    }

    @Test
    @Timeout(8000)
    void parse_withNullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_throwsNullPointerException() {
        String csvData = "a,b,c";
        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(csvData, null));
        assertEquals("format", exception.getMessage());
    }

    // Helper method to access private fields via reflection
    private Object getField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (ReflectiveOperationException e) {
            fail("Reflection failed: " + e.getMessage());
            return null;
        }
    }
}