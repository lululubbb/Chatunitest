package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private URL mockUrl;
    private InputStream mockInputStream;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setup() throws IOException {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);

        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((URL) null, charset, format));
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockUrl, null, format));
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockUrl, charset, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidArguments_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Mock InputStreamReader to avoid real reading
        // Instead of mocking CSVFormat or URL's openStream, use a real InputStreamReader with a dummy InputStream
        // But since InputStream is mocked, InputStreamReader will not throw

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void parse_UrlOpenStreamThrows_PropagatesIOException() throws IOException {
        when(mockUrl.openStream()).thenThrow(new IOException("stream error"));

        IOException ex = assertThrows(IOException.class,
                () -> CSVParser.parse(mockUrl, charset, format));
        assertEquals("stream error", ex.getMessage());
    }
}