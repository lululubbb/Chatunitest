package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_7_1Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = CSVFormat.DEFAULT.withHeader("header1", "header2");
    }

    @AfterEach
    void tearDown() {
        format = null;
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withValidReaderAndFormat() throws IOException {
        String csv = "header1,header2\nvalue1,value2\nvalue3,value4";
        Reader reader = new StringReader(csv);

        CSVParser parser = new CSVParser(reader, format);

        assertNotNull(parser);
        assertEquals(1L, parser.getRecordNumber()); // initial recordNumber is 1 as per constructor
        assertNotNull(parser.getHeaderMap());
        assertEquals(2, parser.getHeaderMap().size());
        assertFalse(parser.isClosed());
        assertEquals("header1", parser.getHeaderMap().keySet().iterator().next());
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withEmptyInput() throws IOException {
        String csv = "";
        Reader reader = new StringReader(csv);

        CSVParser parser = new CSVParser(reader, format);

        assertNotNull(parser);
        assertEquals(1L, parser.getRecordNumber()); // initial recordNumber is 1 as per constructor
        assertNotNull(parser.getHeaderMap());
        assertEquals(2, parser.getHeaderMap().size());
        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withNullFormat_throwsNullPointerException() {
        String csv = "header1,header2\nvalue1,value2";
        Reader reader = new StringReader(csv);

        assertThrows(NullPointerException.class, () -> new CSVParser(reader, null));
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CSVParser(null, format));
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withCustomCharacterOffsetAndRecordNumber() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String csv = "header1,header2\nvalue1,value2";
        Reader reader = new StringReader(csv);

        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class, long.class, long.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(reader, format, 5L, 10L);

        assertNotNull(parser);
        assertEquals(10L, parser.getRecordNumber());
        assertFalse(parser.isClosed());
    }
}