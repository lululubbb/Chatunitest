package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_10_5Test {

    private CSVFormat formatMock;
    private Reader reader;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        reader = new StringReader("header1,header2\nvalue1,value2\n");
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsEmptyListWhenNoRecords() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVParser parser = new CSVParser(reader, formatMock);

        // Use reflection to call private <T> T getRecords(T) with empty list
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Object.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> emptyRecords = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, emptyRecords);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    @Test
    @Timeout(8000)
    void testGetRecords_returnsListWithRecords() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        CSVParser parser = new CSVParser(reader, formatMock);

        // Prepare a list with mocked records
        List<CSVRecord> mockRecords = new ArrayList<>();
        mockRecords.add(record1);
        mockRecords.add(record2);

        // Use reflection to call private <T> T getRecords(T)
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", Object.class);
        getRecordsMethod.setAccessible(true);

        // Invoke with list containing mock records
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(parser, mockRecords);

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result list size should be 2");
        assertSame(record1, result.get(0), "First record should be record1");
        assertSame(record2, result.get(1), "Second record should be record2");
    }

    @Test
    @Timeout(8000)
    void testGetRecords_throwsIOException() throws IOException {
        // Since CSVParser constructor throws IOException, declare throws here

        Reader throwingReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Simulated IO Exception");
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        IOException thrown = assertThrows(IOException.class, () -> {
            CSVParser parser = new CSVParser(throwingReader, formatMock);
            parser.getRecords();
        });
        assertEquals("Simulated IO Exception", thrown.getMessage());
    }
}