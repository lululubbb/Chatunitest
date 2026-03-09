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
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_3_1Test {

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
    void parse_withValidArguments_shouldReturnCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);

        assertEquals(format, actualFormat);

        // Access private field 'lexer' via reflection to verify it is not null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);

        // No exception means success
    }

    @Test
    @Timeout(8000)
    void parse_withNullUrl_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, charset, format);
        });
        assertEquals("url", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullCharset_shouldThrowException() {
        Charset charset = null;
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, format);
        });
        assertEquals("charset", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_shouldThrowException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = null;

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, format);
        });
        assertEquals("format", exception.getMessage());
    }
}