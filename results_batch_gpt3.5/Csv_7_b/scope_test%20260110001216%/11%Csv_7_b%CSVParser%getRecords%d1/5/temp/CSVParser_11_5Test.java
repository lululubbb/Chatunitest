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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_5Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser realParser = constructor.newInstance((java.io.Reader) null, CSVFormat.DEFAULT);
        parser = Mockito.spy(realParser);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // Mock nextRecord() to return 3 records then null
        when(parser.nextRecord())
            .thenReturn(record1, record2, record3, null);

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
    void testGetRecords_withException() throws IOException {
        when(parser.nextRecord()).thenThrow(new IOException("Test exception"));

        List<CSVRecord> records = new ArrayList<>();
        IOException thrown = assertThrows(IOException.class, () -> parser.getRecords(records));
        assertEquals("Test exception", thrown.getMessage());

        verify(parser, times(1)).nextRecord();
    }
}