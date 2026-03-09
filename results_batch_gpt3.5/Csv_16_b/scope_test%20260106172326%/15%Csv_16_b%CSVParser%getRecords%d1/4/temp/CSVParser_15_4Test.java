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

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVParser_15_4Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy Reader and CSVFormat
        Reader dummyReader = mock(Reader.class);
        CSVFormat dummyFormat = mock(CSVFormat.class);
        parser = Mockito.spy(new CSVParser(dummyReader, dummyFormat));

        // By default, nextRecord returns null to simulate no records
        doReturn(null).when(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_NoRecords() throws IOException {
        // nextRecord returns null immediately
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_MultipleRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // Use doReturn chained to simulate multiple returns including null
        doReturn(record1).doReturn(record2).doReturn(record3).doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        assertSame(record3, records.get(2));
        verify(parser, times(4)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_ThrowsIOException() throws IOException {
        // nextRecord throws IOException
        doThrow(new IOException("Test IOException")).when(parser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> {
            parser.getRecords();
        });

        assertEquals("Test IOException", thrown.getMessage());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_PrivateInvocation() throws Exception {
        // Prepare nextRecord to return two records then null
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
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