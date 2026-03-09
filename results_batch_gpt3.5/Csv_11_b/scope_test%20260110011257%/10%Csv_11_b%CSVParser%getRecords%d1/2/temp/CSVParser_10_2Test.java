package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_10_2Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws IOException {
        parser = new CSVParser(new java.io.StringReader(""), mock(CSVFormat.class));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_NoRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_WithRecords() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVParser spyParser = spy(parser);

        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Stub nextRecord() on spyParser to return record1, record2, then null
        doReturn(record1).doReturn(record2).doReturn(null).when(spyParser).nextRecord();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> inputList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(spyParser, inputList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(record1, result.get(0));
        assertSame(record2, result.get(1));
        assertSame(inputList, result);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_ThrowsIOException() throws IOException {
        CSVParser spyParser = spy(parser);

        doThrow(new IOException("IO error")).when(spyParser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> spyParser.getRecords());
        assertEquals("IO error", thrown.getMessage());
    }
}