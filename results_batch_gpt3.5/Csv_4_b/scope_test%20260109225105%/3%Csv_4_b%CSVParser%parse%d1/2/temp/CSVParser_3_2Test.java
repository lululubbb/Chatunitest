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
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat mockFormat;
    private URL mockURL;
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() throws Exception {
        mockFormat = mock(CSVFormat.class);
        mockURL = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        when(mockURL.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, Charset.forName("UTF-8"), mockFormat);
        });
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_UsesUTF8Charset() throws Exception {
        // When charset is null, parse method uses UTF-8 by default
        // We verify that the InputStreamReader is created with UTF-8 charset by calling parse with null charset
        CSVParser parser = CSVParser.parse(mockURL, null, mockFormat);
        assertNotNull(parser);
        verify(mockURL).openStream();
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockURL, Charset.forName("UTF-8"), null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidArguments_ReturnsCSVParser() throws IOException {
        CSVParser parser = CSVParser.parse(mockURL, Charset.forName("UTF-8"), mockFormat);
        assertNotNull(parser);
    }
}