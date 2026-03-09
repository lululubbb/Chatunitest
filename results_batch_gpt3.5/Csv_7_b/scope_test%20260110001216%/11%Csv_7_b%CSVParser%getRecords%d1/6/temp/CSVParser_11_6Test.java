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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_6Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() {
        parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws Exception {
        CSVRecord record1 = Mockito.mock(CSVRecord.class);
        CSVRecord record2 = Mockito.mock(CSVRecord.class);

        // Setup nextRecord to return two records then null
        when(parser.nextRecord())
                .thenReturn(record1)
                .thenReturn(record2)
                .thenReturn(null);

        List<CSVRecord> records = new ArrayList<>();

        // Use reflection to call getRecords(Collection<CSVRecord>) as it's public <T extends Collection<CSVRecord>> T getRecords(T records)
        Method getRecordsMethod = CSVParser.class.getMethod("getRecords", Collection.class);
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, records);

        assertSame(records, result);
        assertEquals(2, records.size());
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));

        verify(parser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNoRecords() throws Exception {
        when(parser.nextRecord()).thenReturn(null);

        List<CSVRecord> records = new ArrayList<>();

        Method getRecordsMethod = CSVParser.class.getMethod("getRecords", Collection.class);
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, records);

        assertSame(records, result);
        assertTrue(records.isEmpty());

        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withIOException() throws Exception {
        when(parser.nextRecord()).thenThrow(new IOException("IO error"));

        List<CSVRecord> records = new ArrayList<>();

        Method getRecordsMethod = CSVParser.class.getMethod("getRecords", Collection.class);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                getRecordsMethod.invoke(parser, records);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap the IOException thrown by nextRecord()
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                } else {
                    throw e;
                }
            }
        });
        assertEquals("IO error", thrown.getMessage());

        verify(parser, times(1)).nextRecord();
    }
}