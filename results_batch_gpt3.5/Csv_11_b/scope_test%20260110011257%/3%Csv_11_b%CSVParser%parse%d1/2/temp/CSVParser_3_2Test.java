package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_3_2Test {

    private URL mockUrl;
    private InputStream mockInputStream;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);

        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_WithValidUrlCharsetAndFormat_ReturnsCSVParser() throws Exception {
        // Act
        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        // Assert
        assertNotNull(parser);
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));
    }

    @Test
    @Timeout(8000)
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, charset, format);
        });
        assertEquals("url", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }
}