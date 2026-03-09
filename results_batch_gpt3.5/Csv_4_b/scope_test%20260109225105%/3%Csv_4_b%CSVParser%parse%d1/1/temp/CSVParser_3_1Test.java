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

class CSVParser_3_1Test {

    private URL mockUrl;
    private InputStream mockInputStream;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws Exception {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
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
        when(mockUrl.openStream()).thenReturn(mockInputStream);

        // Mock InputStreamReader constructor via spy or partial mock is complicated,
        // so we just rely on real constructor here.

        CSVParser parser = null;
        try {
            parser = CSVParser.parse(mockUrl, charset, format);
            assertNotNull(parser);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }

        verify(mockUrl).openStream();
    }
}