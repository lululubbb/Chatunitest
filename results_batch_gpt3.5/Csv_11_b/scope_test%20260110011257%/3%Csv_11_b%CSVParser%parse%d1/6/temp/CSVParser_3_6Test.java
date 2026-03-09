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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_3_6Test {

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
    void testParse_NullUrl_ThrowsException() throws Exception {
        Method parseMethod = CSVParser.class.getMethod("parse", URL.class, Charset.class, CSVFormat.class);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> {
                    try {
                        parseMethod.invoke(null, (Object) null, charset, format);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
        assertEquals("url", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullCharset_ThrowsException() throws Exception {
        Method parseMethod = CSVParser.class.getMethod("parse", URL.class, Charset.class, CSVFormat.class);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> {
                    try {
                        parseMethod.invoke(null, mockUrl, null, format);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
        assertEquals("charset", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullFormat_ThrowsException() throws Exception {
        Method parseMethod = CSVParser.class.getMethod("parse", URL.class, Charset.class, CSVFormat.class);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> {
                    try {
                        parseMethod.invoke(null, mockUrl, charset, null);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_Success() throws Exception {
        Method parseMethod = CSVParser.class.getMethod("parse", URL.class, Charset.class, CSVFormat.class);
        CSVParser result = (CSVParser) parseMethod.invoke(null, mockUrl, charset, format);
        assertNotNull(result);
        assertTrue(result instanceof CSVParser);
    }
}