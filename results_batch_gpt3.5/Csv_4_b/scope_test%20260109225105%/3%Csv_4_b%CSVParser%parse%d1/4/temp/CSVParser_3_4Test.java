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
import java.lang.reflect.Field;
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
    void parse_withValidArgs_shouldReturnCSVParser() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void parse_withNullUrl_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, charset, format);
        });
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullCharset_shouldUseUTF8Charset() throws Exception {
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, null, format);

        assertNotNull(parser);
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_urlOpenStreamThrowsIOException_shouldThrowIOException() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);
        when(mockUrl.openStream()).thenThrow(new IOException("stream error"));

        IOException ex = assertThrows(IOException.class, () -> {
            CSVParser.parse(mockUrl, charset, format);
        });
        assertEquals("stream error", ex.getMessage());
    }
}