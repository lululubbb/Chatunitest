package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.apache.commons.csv.Token.Type.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_10_4Test {

    private CSVFormat formatMock;
    private Reader readerMock;
    private CSVParser parserSpy;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);

        // Use real constructor but spy on the instance to mock internals if needed
        parserSpy = Mockito.spy(new CSVParser(readerMock, formatMock));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_emptyList() throws IOException {
        // Because no input is provided and nextRecord() is not stubbed, getRecords() returns empty list
        List<CSVRecord> records = parserSpy.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withRecords() throws Exception {
        // Prepare a list to pass to getRecords(List)
        List<CSVRecord> recordsList = new ArrayList<>();

        // Use reflection to invoke private generic <T extends List<CSVRecord>> T getRecords(T records)
        Method method = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        method.setAccessible(true);

        // Mock nextRecord() to return one record and then throw NoSuchElementException
        CSVRecord recordMock = mock(CSVRecord.class);
        doReturn(recordMock).doThrow(new NoSuchElementException()).when(parserSpy).nextRecord();

        // Invoke private getRecords(List) method
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) method.invoke(parserSpy, recordsList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(recordMock, result.get(0));
    }
}