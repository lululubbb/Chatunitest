package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, Charset.defaultCharset(), mockFormat);
        });
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() throws IOException {
        URL url = mock(URL.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(url, null, mockFormat);
        });
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() throws IOException {
        URL url = mock(URL.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(url, Charset.defaultCharset(), null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidUrl_ReturnsCSVParser() throws Exception {
        URL url = mock(URL.class);
        // Use a real InputStream to simulate url.openStream()
        InputStream inputStream = new ByteArrayInputStream("header1,header2\nvalue1,value2".getBytes(Charset.defaultCharset()));
        when(url.openStream()).thenReturn(inputStream);

        CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), mockFormat);

        assertNotNull(parser);

        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(mockFormat, actualFormat);

        parser.close();
    }
}