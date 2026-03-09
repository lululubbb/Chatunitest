package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_15_6Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Reader and CSVFormat because CSVParser constructor requires them
        Reader reader = mock(Reader.class);
        CSVFormat format = mock(CSVFormat.class);

        // Create real CSVParser instance with mocks
        CSVParser realParser = new CSVParser(reader, format);

        // Use spy to override nextRecord() method behavior
        CSVParser spyParser = Mockito.spy(realParser);

        // Prepare CSVRecord instances to be returned by nextRecord()
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Setup nextRecord to return two records then null
        doReturn(record1).doReturn(record2).doReturn(null).when(spyParser).nextRecord();

        // Directly assign spyParser to parser field
        this.parser = spyParser;
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsAllRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records, "Returned list should not be null");
        assertEquals(2, records.size(), "Should return two records");
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsEmptyListWhenNoRecords() throws Exception {
        // Spy parser to return null immediately from nextRecord
        CSVParser spyParser = Mockito.spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        // Replace parser instance with spyParser
        this.parser = spyParser;

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records, "Returned list should not be null");
        assertTrue(records.isEmpty(), "List should be empty when no records");
    }

    @Test
    @Timeout(8000)
    void testGetRecords_throwsIOExceptionFromNextRecord() throws Exception {
        CSVParser spyParser = Mockito.spy(parser);
        doThrow(new IOException("Test IO Exception")).when(spyParser).nextRecord();

        this.parser = spyParser;

        IOException thrown = assertThrows(IOException.class, () -> parser.getRecords());
        assertEquals("Test IO Exception", thrown.getMessage());
    }
}