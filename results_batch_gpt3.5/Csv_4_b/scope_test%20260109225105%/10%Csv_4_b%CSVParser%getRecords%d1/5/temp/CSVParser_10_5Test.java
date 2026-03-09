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

public class CSVParser_10_5Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws Exception {
        // Spy CSVParser with a real CSVFormat and empty input
        csvParser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords() throws Exception {
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        // Stub nextRecord() to return 3 records then null
        doReturn(rec1, rec2, rec3, null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(rec1, records.get(0));
        assertSame(rec2, records.get(1));
        assertSame(rec3, records.get(2));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNoRecords() throws Exception {
        // Stub nextRecord() to return null immediately
        doReturn((CSVRecord) null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_privateMethodInvocation() throws Exception {
        // Use reflection to get package-private nextRecord() method (no modifier)
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // Create a fresh spy for this test to avoid interference
        CSVParser localParser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));

        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);

        // Stub nextRecord() calls with multiple returns
        doReturn(rec1, rec2, null).when(localParser).nextRecord();

        // Invoke getRecords normally
        List<CSVRecord> records = localParser.getRecords();

        assertEquals(2, records.size());
        assertSame(rec1, records.get(0));
        assertSame(rec2, records.get(1));

        // After exhaustion, nextRecord() should return null
        Object result = nextRecordMethod.invoke(localParser);
        assertNull(result);
    }
}