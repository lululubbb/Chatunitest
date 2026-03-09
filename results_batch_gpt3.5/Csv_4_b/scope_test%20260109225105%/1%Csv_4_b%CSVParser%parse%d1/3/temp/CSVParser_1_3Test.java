package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_1_3Test {

    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        format = CSVFormat.DEFAULT; // Use a real CSVFormat instance instead of mock
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidFile_returnsCSVParser() throws Exception {
        File tempFile = File.createTempFile("temp", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, format);

        assertNotNull(parser);
        assertSame(format, getField(parser, "format"));
    }

    @Test
    @Timeout(8000)
    public void testParse_nullFile_throwsNullPointerException() {
        File file = null;
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(file, format);
        });
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParse_nullFormat_throwsNullPointerException() {
        File file = new File("dummy.csv");
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(file, null);
        });
        assertEquals("format", ex.getMessage());
    }

    // Helper method to access private fields via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object obj, String fieldName) throws Exception {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}