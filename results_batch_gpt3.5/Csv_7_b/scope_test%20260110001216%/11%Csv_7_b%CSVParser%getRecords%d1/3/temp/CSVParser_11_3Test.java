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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_3Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws IOException {
        csvParser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyRecordsCollection() throws IOException, NoSuchFieldException, IllegalAccessException {
        List<CSVRecord> records = new ArrayList<>();

        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Use reflection to replace the private nextRecord method behavior
        Field nextRecordMethodField = CSVParser.class.getDeclaredField("nextRecord");
        // nextRecord is a method, so we cannot replace it by field; instead, we mock the spy's nextRecord method directly:
        doReturn(record1)
            .doReturn(record2)
            .doReturn(null)
            .when(csvParser).nextRecord();

        List<CSVRecord> result = csvParser.getRecords(records);

        assertSame(records, result);
        assertEquals(2, records.size());
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));
        verify(csvParser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNonEmptyRecordsCollection() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        CSVRecord existingRecord = mock(CSVRecord.class);
        records.add(existingRecord);

        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1)
            .doReturn(record2)
            .doReturn(null)
            .when(csvParser).nextRecord();

        List<CSVRecord> result = csvParser.getRecords(records);

        assertSame(records, result);
        assertEquals(3, records.size());
        assertTrue(records.contains(existingRecord));
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));
        verify(csvParser, times(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_whenNextRecordThrowsIOException() throws IOException {
        List<CSVRecord> records = new ArrayList<>();

        doThrow(new IOException("Read error")).when(csvParser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> csvParser.getRecords(records));
        assertEquals("Read error", thrown.getMessage());
        verify(csvParser).nextRecord();
    }
}