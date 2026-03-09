package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
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
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        mockUrl = mock(URL.class);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_shouldReturnCSVParser_whenValidArguments() throws IOException, NoSuchFieldException, IllegalAccessException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockUrl.openStream()).thenReturn(mockInputStream);

        // Mock read() method of InputStream to return -1 (EOF) to prevent blocking
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);
        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object parserFormat = formatField.get(parser);
        assertSame(format, parserFormat);

        verify(mockUrl).openStream();
    }

    @Test
    @Timeout(8000)
    void parse_shouldThrowNullPointerException_whenUrlIsNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, charset, format);
        });
        assertEquals("url", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_shouldThrowNullPointerException_whenCharsetIsNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_shouldThrowNullPointerException_whenFormatIsNull() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_shouldThrowIOException_whenOpenStreamFails() throws IOException {
        when(mockUrl.openStream()).thenThrow(new IOException("stream error"));

        IOException thrown = assertThrows(IOException.class, () -> {
            CSVParser.parse(mockUrl, charset, format);
        });
        assertEquals("stream error", thrown.getMessage());
    }
}