package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_6Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);

        // Use reflection to set the private reusableToken field to avoid NullPointerException in nextRecord()
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, new Token());

        // Also set the private field 'record' to avoid potential NPE if used internally
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(parser, new ArrayList<String>());

        // Set private 'recordNumber' field to 0 to avoid issues
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // Setup nextRecord() to return 3 records and then null
        when(parser.nextRecord())
            .thenReturn(record1)
            .thenReturn(record2)
            .thenReturn(record3)
            .thenReturn(null);

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
        when(parser.nextRecord()).thenReturn(null);

        List<CSVRecord> records = new ArrayList<>();
        List<CSVRecord> result = parser.getRecords(records);

        assertSame(records, result);
        assertTrue(records.isEmpty());

        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withDifferentCollectionType() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);

        when(parser.nextRecord())
            .thenReturn(record1)
            .thenReturn(null);

        // Use LinkedList to test generic collection type
        java.util.LinkedList<CSVRecord> linkedList = new java.util.LinkedList<>();
        java.util.Collection<CSVRecord> result = parser.getRecords(linkedList);

        assertSame(linkedList, result);
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.contains(record1));

        verify(parser, times(2)).nextRecord();
    }
}