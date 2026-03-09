package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedHashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_16_3Test {

    private CSVParser csvParser;
    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNull() throws Throwable {
        when(format.getHeader()).thenReturn(null);
        when(format.getIgnoreHeaderCase()).thenReturn(false);

        csvParser = new CSVParser(mock(java.io.Reader.class), format);

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmptyAndNextRecordNull() throws Throwable {
        when(format.getHeader()).thenReturn(new String[0]);
        when(format.getIgnoreHeaderCase()).thenReturn(false);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), format));
        doReturn(null).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderEmptyAndNextRecordReturnsValues() throws Throwable {
        when(format.getHeader()).thenReturn(new String[0]);
        when(format.getIgnoreHeaderCase()).thenReturn(true);

        CSVRecord record = mock(CSVRecord.class);
        when(record.values()).thenReturn(java.util.Arrays.asList("A", "B", "C"));

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), format));
        doReturn(record).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(3, result.size());
        assertEquals(0, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(2, result.get("C"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_formatHeaderNonEmptySkipHeaderRecordTrue() throws Throwable {
        String[] headers = new String[] {"X", "Y"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(true);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), format));
        doReturn(mock(CSVRecord.class)).when(csvParser).nextRecord();

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(2, result.size());
        assertEquals(0, result.get("X"));
        assertEquals(1, result.get("Y"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_duplicateHeaderThrows() throws Throwable {
        String[] headers = new String[] {"dup", "dup"};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(false);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), format));

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(csvParser);
        });

        // InvocationTargetException wraps the IllegalArgumentException
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertTrue(cause.getMessage().contains("duplicate name"));
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_duplicateEmptyHeaderAllowed() throws Throwable {
        String[] headers = new String[] {"", ""};
        when(format.getHeader()).thenReturn(headers);
        when(format.getIgnoreHeaderCase()).thenReturn(false);
        when(format.getSkipHeaderRecord()).thenReturn(false);
        when(format.getAllowMissingColumnNames()).thenReturn(true);

        csvParser = spy(new CSVParser(mock(java.io.Reader.class), format));

        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);

        Map<String, Integer> result = (Map<String, Integer>) method.invoke(csvParser);

        assertNotNull(result);
        assertEquals(1, result.size());
        // Because keys are the same empty string, the last put overwrites the first,
        // so the map contains only one entry with key "" and value 1.
        assertEquals(1, result.get(""));
    }
}