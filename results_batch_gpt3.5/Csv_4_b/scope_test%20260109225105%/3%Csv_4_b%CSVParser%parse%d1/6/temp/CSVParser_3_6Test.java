package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private URL mockUrl;
    private InputStream mockInputStream;

    @BeforeEach
    void setup() throws IOException {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);

        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void testParse_withValidArguments_shouldReturnCSVParser() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);
        assertEquals(format, getField(parser, "format"));
        assertNotNull(getField(parser, "headerMap"));
    }

    @Test
    @Timeout(8000)
    void testParse_nullUrl_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, charset, format);
        });
        assertEquals("url", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_nullCharset_shouldUseUTF8Charset() throws IOException {
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, null, format);

        assertNotNull(parser);
        assertEquals(format, getField(parser, "format"));
    }

    @Test
    @Timeout(8000)
    void testParse_nullFormat_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", exception.getMessage());
    }

    private Object getField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Reflection error: " + e.getMessage());
            return null;
        }
    }
}