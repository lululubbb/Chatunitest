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

import org.junit.jupiter.api.Test;

class CSVParser_3_5Test {

    @Test
    @Timeout(8000)
    void testParse_withValidUrlCharsetAndFormat() throws Exception {
        // Arrange
        URL mockUrl = mock(URL.class);
        InputStream mockInputStream = mock(InputStream.class);
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        when(mockUrl.openStream()).thenReturn(mockInputStream);

        // Act
        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        // Assert
        assertNotNull(parser);

        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertSame(format, actualFormat);

        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullUrl_throwsException() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, charset, format);
        });
        assertEquals("url", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullCharset_throwsException() throws Exception {
        URL mockUrl = mock(URL.class);
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsException() throws Exception {
        URL mockUrl = mock(URL.class);
        Charset charset = Charset.forName("UTF-8");

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withUrlOpenStreamThrowsIOException() throws Exception {
        URL mockUrl = mock(URL.class);
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        when(mockUrl.openStream()).thenThrow(new IOException("Stream error"));

        IOException thrown = assertThrows(IOException.class, () -> {
            CSVParser.parse(mockUrl, charset, format);
        });
        assertEquals("Stream error", thrown.getMessage());
    }
}