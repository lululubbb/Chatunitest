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

class CSVParser_3_6Test {

    private URL mockUrl;
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() throws IOException {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, charset, format);
        });
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() {
        CSVFormat format = mock(CSVFormat.class);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        Charset charset = Charset.forName("UTF-8");
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidParameters_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);
        // Check that the parser has the format set correctly via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }
}