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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

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
    void parse_WithValidArguments_ShouldReturnCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(format, actualFormat);

        assertFalse(parser.isClosed());

        verify(mockUrl).openStream();
        verifyNoMoreInteractions(mockUrl);
    }

    @Test
    @Timeout(8000)
    void parse_WithNullUrl_ShouldThrowException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> CSVParser.parse(null, charset, format));
        assertEquals("url must not be null", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_WithNullCharset_ShouldUseUTF8Charset() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, null, format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(format, actualFormat);

        verify(mockUrl).openStream();
    }

    @Test
    @Timeout(8000)
    void parse_WithNullFormat_ShouldThrowException() {
        Charset charset = Charset.forName("UTF-8");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> CSVParser.parse(mockUrl, charset, null));
        assertEquals("format must not be null", thrown.getMessage());
    }
}