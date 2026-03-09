package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

class CSVParser_2_4Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(csvData, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertNotNull(parser.getHeaderMap());
        assertTrue(parser.getHeaderMap() instanceof java.util.Map);
    }

    @Test
    @Timeout(8000)
    void testParse_nullString_throwsException() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_nullFormat_throwsException() {
        String csvData = "a,b";

        NullPointerException exception = assertThrows(NullPointerException.class, () -> CSVParser.parse(csvData, null));
        assertEquals("format", exception.getMessage());
    }
}