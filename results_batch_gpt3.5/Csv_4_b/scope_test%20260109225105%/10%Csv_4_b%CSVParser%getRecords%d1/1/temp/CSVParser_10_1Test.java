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

class CSVParser_10_1Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVParser instance with minimal valid input and format
        parser = spy(new CSVParser(new java.io.StringReader("a,b\n1,2"), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        verify(parser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNoRecords() throws Exception {
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_invokesPrivateNextRecordViaReflection() throws Exception {
        // Create a real CSVParser instance
        CSVParser realParser = new CSVParser(new java.io.StringReader("a,b\n1,2"), CSVFormat.DEFAULT);
        // Spy on the real instance
        CSVParser spyParser = spy(realParser);

        // Use reflection to get the private nextRecord method
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // Use doAnswer to invoke the real private method via reflection when nextRecord() is called
        doAnswer(invocation -> nextRecordMethod.invoke(spyParser)).when(spyParser).nextRecord();

        List<CSVRecord> records = spyParser.getRecords();

        assertNotNull(records);
        assertFalse(records.isEmpty());
    }
}