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

public class CSVParser_10_2Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() {
        parser = Mockito.mock(CSVParser.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        when(parser.nextRecord())
            .thenReturn(record1)
            .thenReturn(record2)
            .thenReturn(null);

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        verify(parser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNoRecords() throws Exception {
        when(parser.nextRecord()).thenReturn(null);

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_nextRecordThrowsIOException() throws Exception {
        when(parser.nextRecord()).thenThrow(new IOException("IO failure"));

        IOException thrown = assertThrows(IOException.class, () -> {
            parser.getRecords();
        });

        assertEquals("IO failure", thrown.getMessage());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testInvokePrivateNextRecordMethodUsingReflection() throws Exception {
        // Create a real CSVParser instance with minimal input
        CSVParser realParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        CSVRecord result = (CSVRecord) nextRecordMethod.invoke(realParser);

        assertNull(result);
    }
}