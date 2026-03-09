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

public class CSVParser_10_3Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a real instance with a dummy Reader and CSVFormat to avoid NPEs
        parser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);
        // Spy on the real parser to stub nextRecord method
        parser = Mockito.spy(parser);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_multipleRecords() throws Exception {
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        // Use reflection to get the package-private nextRecord method
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // Stub the package-private nextRecord method on the spy using doAnswer via reflection
        doAnswer(invocation -> rec1)
            .doAnswer(invocation -> rec2)
            .doAnswer(invocation -> rec3)
            .doAnswer(invocation -> null)
            .when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(rec1, records.get(0));
        assertSame(rec2, records.get(1));
        assertSame(rec3, records.get(2));
        verify(parser, times(4)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_noRecords() throws Exception {
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testNextRecord_privateMethodInvocation() throws Exception {
        // Using reflection to invoke package-private nextRecord method
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // Use a real parser instance with dummy Reader and CSVFormat
        CSVParser realParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        try {
            Object result = nextRecordMethod.invoke(realParser);
            // result can be null or CSVRecord
            assertTrue(result == null || result instanceof CSVRecord);
        } catch (Exception e) {
            fail("Invocation of nextRecord threw exception: " + e.getMessage());
        }
    }
}