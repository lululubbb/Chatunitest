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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

class CSVParser_4_3Test {

    @Test
    @Timeout(8000)
    void testParse_withValidReaderAndFormat_createsCSVParser() throws IOException {
        CSVFormat format = mock(CSVFormat.class);
        String csvContent = "header1,header2\nvalue1,value2";
        Reader reader = new StringReader(csvContent);

        CSVParser parser = CSVParser.parse(reader, format);

        assertNotNull(parser);
        assertSame(format, getField(parser, "format"));
        assertFalse(parser.isClosed());
        parser.close(); // close parser to avoid resource leak
    }

    @Test
    @Timeout(8000)
    void testParse_readerThrowsIOException_propagatesException() throws IOException {
        CSVFormat format = mock(CSVFormat.class);
        Reader reader = mock(Reader.class);

        doThrow(new IOException("read error")).when(reader).read(any(char[].class), anyInt(), anyInt());

        IOException thrown = assertThrows(IOException.class, () -> {
            try (CSVParser parser = CSVParser.parse(reader, format)) {
                // no-op
            }
        });
        assertEquals("read error", thrown.getMessage());
    }

    // Reflection helper to get private field values
    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to get field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }
}