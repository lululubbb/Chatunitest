package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_2Test {

    private CSVFormat format;

    @BeforeEach
    void setup() {
        format = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_WithValidReaderAndFormat() throws IOException {
        String csvData = "a,b,c\n1,2,3\n4,5,6";
        Reader reader = new StringReader(csvData);

        CSVParser parser = CSVParser.parse(reader, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertEquals(format, getField(parser, "format"));
        assertNotNull(getField(parser, "headerMap"));
        assertNotNull(getField(parser, "lexer"));
        assertEquals(0L, parser.getRecordNumber());
        assertEquals(0L, (long) getField(parser, "characterOffset"));
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullReader_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse((Reader) null, format));
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullFormat_ThrowsNullPointerException() {
        Reader reader = new StringReader("a,b,c");
        assertThrows(NullPointerException.class, () -> CSVParser.parse(reader, null));
    }

    @Test
    @Timeout(8000)
    void testParse_PrivateConstructorInvocation() throws Exception {
        String csvData = "x,y\n7,8";
        Reader reader = new StringReader(csvData);

        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class, long.class, long.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(reader, format, 10L, 5L);

        assertNotNull(parser);
        assertEquals(10L, (long) getField(parser, "characterOffset"));
        assertEquals(5L, parser.getRecordNumber());
        assertEquals(format, getField(parser, "format"));
    }

    // Helper method to access private fields via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}