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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_10_6Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws IOException {
        csvParser = spy(new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class)));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withMultipleRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Stub nextRecord() to return two records then null
        doReturn(record1).doReturn(record2).doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNoRecords() throws IOException {
        // Stub nextRecord() to return null immediately
        doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testInvokePrivateAddRecordValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // Just invoke to ensure no exception is thrown
        addRecordValueMethod.invoke(csvParser);
    }
}