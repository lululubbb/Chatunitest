package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_9_1Test {

    private CSVParser parser;
    private Iterator<CSVRecord> iterator;

    @BeforeEach
    public void setUp() throws IOException {
        String csvData = "header1,header2\nvalue1,value2\nvalue3,value4";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(csvData), format);
        iterator = parser.iterator();
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_initial() {
        // Initially, before any record is read, recordNumber should be 0
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_afterReadingRecords() {
        // Read first record
        iterator.next();
        assertEquals(1L, parser.getRecordNumber());

        // Read second record
        iterator.next();
        assertEquals(2L, parser.getRecordNumber());
    }
}