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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_2Test {

    CSVParser parser;
    Method nextRecordMethod;

    @BeforeEach
    void setUp() throws Exception {
        parser = Mockito.spy(new CSVParser(null, null));

        // Use reflection to get the nextRecord method
        nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        final int[] count = {0};
        final int max = 3;

        // Use doAnswer with reflection invoke to avoid compile error on nextRecord() mocking
        doAnswer(invocation -> {
            if (count[0] < max) {
                count[0]++;
                return mock(CSVRecord.class);
            }
            return null;
        }).when(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyCollection() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        // Call getRecords with the spy parser that has nextRecord mocked
        List<CSVRecord> result = parser.getRecords(records);
        assertSame(records, result);
        assertEquals(3, records.size());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNonEmptyCollection() throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        records.add(mock(CSVRecord.class));
        List<CSVRecord> result = parser.getRecords(records);
        assertSame(records, result);
        assertEquals(4, records.size());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_handlesIOException() throws IOException {
        CSVParser throwingParser = Mockito.spy(new CSVParser(null, null));
        doThrow(new IOException("forced")).when(throwingParser).nextRecord();
        List<CSVRecord> records = new ArrayList<>();
        IOException thrown = assertThrows(IOException.class, () -> {
            throwingParser.getRecords(records);
        });
        assertEquals("forced", thrown.getMessage());
    }
}