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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_6_6Test {

    private URL mockUrl;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        mockUrl = mock(URL.class);
        charset = Charset.forName("UTF-8");
        format = CSVFormat.DEFAULT;

        // Provide a real InputStream with valid CSV content
        String csvContent = "header1,header2\nvalue1,value2\n";
        InputStream mockInputStream = new ByteArrayInputStream(csvContent.getBytes(charset));
        when(mockUrl.openStream()).thenReturn(mockInputStream);
    }

    @Test
    @Timeout(8000)
    void parse_validUrl_returnsCSVParser() throws Exception {
        CSVParser parser = CSVParser.parse(mockUrl, charset, format);

        assertNotNull(parser);

        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);

        assertEquals(format, parserFormat);
    }

    @Test
    @Timeout(8000)
    void parse_nullUrl_throwsException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse((URL) null, charset, format));
    }

    @Test
    @Timeout(8000)
    void parse_nullCharset_throwsException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse(mockUrl, null, format));
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse(mockUrl, charset, null));
    }
}