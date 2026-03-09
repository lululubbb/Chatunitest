package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private URL mockUrl;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        mockUrl = mock(URL.class);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, charset, format);
        });
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidArguments_ReturnsCSVParser() throws IOException {
        // Instead of mocking URL.openStream(), use a real URL to a resource or create a temporary file
        URL url = getClass().getResource("/test.csv");
        if (url == null) {
            fail("Test resource /test.csv not found");
        }
        CSVParser parser = CSVParser.parse(url, charset, format);
        assertNotNull(parser);
    }
}