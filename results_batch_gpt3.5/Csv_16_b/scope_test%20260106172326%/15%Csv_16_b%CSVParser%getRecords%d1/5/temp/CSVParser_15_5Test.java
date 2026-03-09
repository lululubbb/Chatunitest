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
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_15_5Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVFormat instance using the public static field CSVFormat.DEFAULT
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a real Reader instance with empty content
        Reader reader = new java.io.StringReader("");

        // Use the constructor with Reader and CSVFormat
        parser = Mockito.spy(new CSVParser(reader, format));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsEmptyList_whenNoRecords() throws IOException {
        // Mock nextRecord() to return null immediately on spy
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(parser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsMultipleRecords() throws IOException {
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        final CSVRecord[] responses = new CSVRecord[] { rec1, rec2, rec3, null };
        final int[] count = {0};
        doAnswer(invocation -> {
            CSVRecord r = responses[count[0]];
            count[0]++;
            return r;
        }).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(rec1, records.get(0));
        assertSame(rec2, records.get(1));
        assertSame(rec3, records.get(2));
        verify(parser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_propagatesIOException() throws IOException {
        // Mock nextRecord() to throw IOException
        doThrow(new IOException("Test IO Exception")).when(parser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> parser.getRecords());
        assertEquals("Test IO Exception", thrown.getMessage());
        verify(parser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testInvokePrivateNextRecordUsingReflection() throws Exception {
        // Use reflection to invoke private nextRecord method
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        Object result = nextRecordMethod.invoke(parser);
        assertTrue(result == null || result instanceof CSVRecord);
    }
}