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
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVParser_10_4Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy CSVFormat
        CSVFormat format = mock(CSVFormat.class);
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), format));
        // Suppress actual nextRecord() calls to allow mocking
        doReturn(null).when(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecordsWithMultipleRecords() throws Exception {
        // Prepare mock CSVRecord instances
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // Mock nextRecord() to return three records then null
        doReturn(record1).doReturn(record2).doReturn(record3).doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        assertSame(record3, records.get(2));
    }

    @Test
    @Timeout(8000)
    public void testGetRecordsWithNoRecords() throws Exception {
        // Mock nextRecord() to return null immediately
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordsThrowsIOException() throws Exception {
        // Mock nextRecord() to throw IOException
        doThrow(new IOException("Test IO Exception")).when(parser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> parser.getRecords());

        assertEquals("Test IO Exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testInvokeGetRecordsViaReflection() throws Exception {
        // Prepare mock CSVRecord instances
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Mock nextRecord() to return two records then null
        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords");
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> records = (List<CSVRecord>) getRecordsMethod.invoke(parser);

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
    }
}