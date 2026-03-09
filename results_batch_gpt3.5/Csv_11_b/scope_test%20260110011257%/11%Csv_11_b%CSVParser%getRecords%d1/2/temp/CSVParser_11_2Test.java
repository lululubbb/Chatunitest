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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVParser_11_2Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a real CSVParser instance with a dummy Reader and CSVFormat to allow spying
        csvParser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_NextRecordReturnsMultipleRecords() throws Exception {
        // Prepare mock CSVRecord objects
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        // Stub nextRecord() to return rec1, rec2, rec3, then null
        doReturn(rec1).doReturn(rec2).doReturn(rec3).doReturn(null).when(csvParser).nextRecord();

        // Prepare a collection to collect records
        List<CSVRecord> records = new ArrayList<>();

        // Use reflection to get the getRecords method with Collection parameter
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(csvParser, records);

        // Verify all records are added
        assertEquals(3, result.size());
        assertSame(rec1, result.get(0));
        assertSame(rec2, result.get(1));
        assertSame(rec3, result.get(2));

        // Verify nextRecord() called 4 times (3 records + 1 null)
        verify(csvParser, times(4)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_NextRecordReturnsNullImmediately() throws Exception {
        // Stub nextRecord() to return null immediately
        doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = new ArrayList<>();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(csvParser, records);

        // No records added
        assertTrue(result.isEmpty());

        // Verify nextRecord() called once
        verify(csvParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_ThrowsIOException() throws Exception {
        // Stub nextRecord() to throw IOException
        doThrow(new IOException("Test IO Exception")).when(csvParser).nextRecord();

        List<CSVRecord> records = new ArrayList<>();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getRecordsMethod.invoke(csvParser, records);
        });

        // The reflection invocation wraps IOException in InvocationTargetException, unwrap it
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IOException);
        assertEquals("Test IO Exception", cause.getMessage());

        // Verify nextRecord() called once
        verify(csvParser, times(1)).nextRecord();
    }
}