package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private URL mockUrl;
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() throws Exception {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((URL) null, Charset.defaultCharset(), CSVFormat.DEFAULT));
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockUrl, null, CSVFormat.DEFAULT));
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockUrl, Charset.defaultCharset(), null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidInputs_ReturnsCSVParser() throws IOException {
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        when(mockUrl.openStream()).thenReturn(mockInputStream);

        // We must mock the InputStreamReader constructor to avoid actual IO or use a spy on CSVParser
        // But since CSVParser creates InputStreamReader internally, we cannot inject it.
        // So instead, we mock the InputStream to return some data to avoid NullPointerException inside CSVParser.
        // Here, we mock the InputStream to return -1 immediately (empty stream)
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);
        assertNotNull(parser);

        Reader readerField = (Reader) getField(parser, "reader");
        assertNotNull(readerField);

        CSVFormat actualFormat = (CSVFormat) getField(parser, "format");
        assertSame(format, actualFormat);

        verify(mockUrl).openStream();
    }

    // Reflection helper
    private Object getField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
            return null;
        }
    }
}