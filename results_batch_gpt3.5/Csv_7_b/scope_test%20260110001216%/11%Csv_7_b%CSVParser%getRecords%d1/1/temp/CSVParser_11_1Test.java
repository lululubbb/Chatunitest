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
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class CSVParser_11_1Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws IOException {
        // Use a spy with CALLS_REAL_METHODS to allow real method calls and stubbing
        csvParser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withEmptyCollection() throws Exception {
        List<CSVRecord> records = new ArrayList<>();

        // Setup nextRecord() to return null immediately
        doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> result = csvParser.getRecords(records);

        assertSame(records, result);
        assertTrue(result.isEmpty());

        verify(csvParser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords() throws Exception {
        List<CSVRecord> records = new ArrayList<>();
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Setup nextRecord() to return two records then null
        doReturn(record1).doReturn(record2).doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> result = csvParser.getRecords(records);

        assertSame(records, result);
        assertEquals(2, result.size());
        assertEquals(record1, result.get(0));
        assertEquals(record2, result.get(1));

        verify(csvParser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNullCollection_throwsException() throws Exception {
        // The method does not explicitly check for null, so it will throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            csvParser.getRecords(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_usingReflection() throws Exception {
        List<CSVRecord> records = new ArrayList<>();
        CSVRecord record = mock(CSVRecord.class);

        doReturn(record).doReturn(null).when(csvParser).nextRecord();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Collection.class);
        getRecordsMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(csvParser, records);

        assertSame(records, result);
        assertEquals(1, result.size());
        assertEquals(record, result.get(0));

        verify(csvParser, times(2)).nextRecord();
    }
}