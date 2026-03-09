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
import java.net.URL;
import java.nio.charset.Charset;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

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
    void parse_NullUrl_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((URL) null, charset, format));
        assertEquals("url", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullCharset_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(mockUrl, null, format));
        assertEquals("charset", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(mockUrl, charset, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidParameters_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        InputStream emptyInputStream = InputStream.nullInputStream();
        URL realUrl = mock(URL.class);
        when(realUrl.openStream()).thenReturn(emptyInputStream);

        CSVParser result = CSVParser.parse(realUrl, charset, format);

        assertNotNull(result);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(result);

        assertEquals(format, actualFormat);
    }
}