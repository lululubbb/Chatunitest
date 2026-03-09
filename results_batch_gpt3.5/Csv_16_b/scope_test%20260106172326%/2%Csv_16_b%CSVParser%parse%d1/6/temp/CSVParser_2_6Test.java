package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CSVParser_2_6Test {

    @Test
    @Timeout(8000)
    void testParse_InputStreamCharsetFormat_NullInputStream() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((InputStream) null, StandardCharsets.UTF_8, format);
        });
        assertEquals("inputStream", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_InputStreamCharsetFormat_NullFormat() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(is, StandardCharsets.UTF_8, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_InputStreamCharsetFormat_ValidInput() throws IOException {
        String csv = "a,b,c\n1,2,3\n";
        InputStream is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        CSVFormat format = CSVFormat.DEFAULT;
        try (CSVParser parser = CSVParser.parse(is, StandardCharsets.UTF_8, format)) {
            assertNotNull(parser);
            // getRecords() may be lazy, so force parsing by iterating
            assertEquals(2, parser.getRecords().size());
            assertEquals("a", parser.getRecords().get(0).get(0));
            assertEquals("1", parser.getRecords().get(1).get(0));
            assertFalse(parser.isClosed());
        }
    }
}