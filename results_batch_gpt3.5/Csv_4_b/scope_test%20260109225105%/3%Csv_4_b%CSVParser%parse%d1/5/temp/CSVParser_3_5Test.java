package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_3_5Test {

    private URL mockUrl;

    @BeforeEach
    void setUp() throws Exception {
        mockUrl = mock(URL.class);
        // Return a non-null InputStream to avoid NPE in CSVParser.parse
        InputStream is = new ByteArrayInputStream("a,b\n1,2".getBytes(Charset.forName("UTF-8")));
        when(mockUrl.openStream()).thenReturn(is);
    }

    @Test
    @Timeout(8000)
    void testParse_NullUrl() {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException npe = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, charset, format);
        });
        assertEquals("url", npe.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullCharset() throws IOException {
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException npe = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, null, format);
        });
        assertEquals("charset", npe.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullFormat() {
        Charset charset = Charset.forName("UTF-8");

        NullPointerException npe = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockUrl, charset, null);
        });
        assertEquals("format", npe.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_ValidInputs() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CSVFormat format = mock(CSVFormat.class);

        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);
    }
}