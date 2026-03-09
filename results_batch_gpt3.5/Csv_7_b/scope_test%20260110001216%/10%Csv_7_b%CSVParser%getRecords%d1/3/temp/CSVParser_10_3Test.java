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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CSVParser_10_3Test {

    private CSVParser parser;

    @Mock
    private Lexer lexerMock;

    private AutoCloseable mocks;

    @BeforeEach
    public void setup() throws Exception {
        mocks = MockitoAnnotations.openMocks(this); // Use openMocks, returns AutoCloseable

        CSVFormat format = CSVFormat.DEFAULT;

        Reader reader = new StringReader("header1,header2\nvalue1,value2\nvalue3,value4");

        parser = new CSVParser(reader, format);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of("header1", 0, "header2", 1));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_returnsList() throws Exception {
        List<CSVRecord> records = new ArrayList<>();

        Method getRecordsTMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsTMethod.setAccessible(true);

        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        CSVParser spyParser = spy(parser);

        doReturn(record1)
            .doReturn(record2)
            .doThrow(new NoSuchElementException())
            .when(spyParser).nextRecord();

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsTMethod.invoke(spyParser, records);

        assertEquals(2, result.size());
        assertSame(record1, result.get(0));
        assertSame(record2, result.get(1));
        assertSame(records, result);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_callsPrivateGetRecordsT() throws IOException {
        CSVParser spyParser = spy(parser);

        List<CSVRecord> mockList = new ArrayList<>();

        try {
            Method getRecordsTMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
            getRecordsTMethod.setAccessible(true);

            doReturn(mockList).when(spyParser).getRecords(any());

            List<CSVRecord> result = spyParser.getRecords();

            assertSame(mockList, result);

            verify(spyParser, times(1)).getRecords(any());
        } catch (Exception e) {
            fail("Reflection exception: " + e.getMessage());
        }
    }
}