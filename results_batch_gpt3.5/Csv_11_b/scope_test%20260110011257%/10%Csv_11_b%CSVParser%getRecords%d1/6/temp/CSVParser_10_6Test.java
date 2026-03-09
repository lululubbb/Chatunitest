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
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_10_6Test {

    private CSVFormat formatMock;
    private Reader readerMock;
    private CSVParser parser;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testGetRecordsReturnsEmptyListWhenNoRecords() throws Exception {
        parser = new CSVParser(readerMock, formatMock);

        CSVParser spyParser = Mockito.spy(parser);

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        // Use doAnswer with reflection invoke on spyParser and method to stub the private getRecords(List)
        doAnswer(invocation -> new ArrayList<CSVRecord>())
            .when(spyParser)
            .getRecords(any(List.class));

        List<CSVRecord> records = spyParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecordsReturnsListFromGetRecordsT() throws Exception {
        parser = new CSVParser(readerMock, formatMock);

        List<CSVRecord> expectedRecords = new ArrayList<>();
        CSVRecord recordMock = mock(CSVRecord.class);
        expectedRecords.add(recordMock);

        CSVParser spyParser = Mockito.spy(parser);

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        doAnswer(invocation -> expectedRecords)
            .when(spyParser)
            .getRecords(any(List.class));

        List<CSVRecord> actualRecords = spyParser.getRecords();

        assertSame(expectedRecords, actualRecords);
        assertEquals(1, actualRecords.size());
        assertEquals(recordMock, actualRecords.get(0));
    }

    @Test
    @Timeout(8000)
    void testGetRecordsThrowsIOExceptionFromGetRecordsT() throws Exception {
        parser = new CSVParser(readerMock, formatMock);

        CSVParser spyParser = Mockito.spy(parser);

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        doThrow(new IOException("test exception"))
            .when(spyParser)
            .getRecords(any(List.class));

        IOException thrown = assertThrows(IOException.class, () -> spyParser.getRecords());
        assertEquals("test exception", thrown.getMessage());
    }
}