package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CSVParser_5_1Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(csvData, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertNotNull(parser.getHeaderMap());
        assertEquals(1, parser.getRecords().size());
        CSVRecord record = parser.getRecords().get(0);
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        String csvData = "a,b\n1,2";
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(csvData, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withEmptyString_returnsParserWithNoRecords() throws IOException {
        String csvData = "";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(csvData, format);

        assertNotNull(parser);
        assertTrue(parser.getRecords().isEmpty());
    }
}