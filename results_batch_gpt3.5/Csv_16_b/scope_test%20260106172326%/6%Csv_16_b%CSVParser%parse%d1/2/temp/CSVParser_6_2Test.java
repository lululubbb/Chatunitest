package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private URL mockUrl;
    private InputStream mockInputStream;

    @BeforeEach
    void setup() throws Exception {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        when(mockUrl.openStream()).thenReturn(mockInputStream);
        // When creating InputStreamReader, it will read from mockInputStream,
        // so mockInputStream must not throw when read is called.
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
    }

    @Test
    @Timeout(8000)
    void parse_nullUrl_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, Charset.defaultCharset(), CSVFormat.DEFAULT);
        });
        assertEquals("url", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullCharset_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, CSVFormat.DEFAULT);
        });
        assertEquals("charset", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, Charset.defaultCharset(), null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_validArguments_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));

        verify(mockUrl).openStream();
    }
}