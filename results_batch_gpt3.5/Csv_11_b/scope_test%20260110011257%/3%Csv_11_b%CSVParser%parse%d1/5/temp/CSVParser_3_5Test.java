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
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_3_5Test {

    private URL mockUrl;
    private InputStream mockInputStream;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        mockUrl = mock(URL.class);
        mockInputStream = mock(InputStream.class);
        when(mockUrl.openStream()).thenReturn(mockInputStream);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParse_withValidParameters_returnsCSVParser() throws Exception {
        // Use reflection to call Assertions.notNull to avoid mocking static methods
        Method notNullMethod = null;
        for (Method m : Class.forName("org.apache.commons.csv.Assertions").getDeclaredMethods()) {
            if (m.getName().equals("notNull") && m.getParameterCount() == 2) {
                notNullMethod = m;
                break;
            }
        }
        assertNotNull(notNullMethod);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());
        verify(mockUrl, times(1)).openStream();
    }

    @Test
    @Timeout(8000)
    void testParse_withNullUrl_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((URL) null, charset, format);
        });
    }

    @Test
    @Timeout(8000)
    void testParse_withNullCharset_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
    }
}