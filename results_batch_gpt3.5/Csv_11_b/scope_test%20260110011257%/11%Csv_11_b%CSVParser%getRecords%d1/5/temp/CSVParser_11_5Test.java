package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVParser_11_5Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        // Mock CSVFormat as it is used in constructor
        CSVFormat format = mock(CSVFormat.class);
        // Create a dummy Reader (not used directly in getRecords)
        Reader reader = mock(Reader.class);
        // Create a spy of CSVParser to mock nextRecord()
        parser = Mockito.spy(new CSVParser(reader, format));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // Setup nextRecord() to return 3 records then null
        when(parser.nextRecord()).thenReturn(record1, record2, record3, null);

        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertEquals(3, records.size());
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));
        assertTrue(records.contains(record3));

        verify(parser, times(4)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNoRecords() throws IOException {
        // nextRecord returns null immediately
        when(parser.nextRecord()).thenReturn((CSVRecord) null);

        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertTrue(records.isEmpty());

        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withDifferentCollectionType() throws IOException {
        // nextRecord returns one record then null
        CSVRecord record = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(record, (CSVRecord) null);

        // Use ArrayList as collection
        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertEquals(1, records.size());
        assertEquals(record, records.get(0));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_reflectionInvocation() throws Exception {
        // nextRecord returns two records then null
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(record1, record2, (CSVRecord) null);

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> records = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, records);

        assertSame(records, result);
        assertEquals(2, records.size());
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));
    }
}