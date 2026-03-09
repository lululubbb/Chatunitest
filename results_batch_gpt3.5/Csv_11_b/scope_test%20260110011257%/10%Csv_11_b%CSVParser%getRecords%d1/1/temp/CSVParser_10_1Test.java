package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVParser_10_1Test {

    private CSVParser parser;
    private Reader reader;

    @BeforeEach
    public void setUp() throws IOException {
        reader = new StringReader("header1,header2\nvalue1,value2\nvalue3,value4");
        parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (parser != null && !parser.isClosed()) {
            parser.close();
        }
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_noArg_returnsListOfCSVRecord() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());

        CSVRecord first = records.get(0);
        assertEquals("value1", first.get("header1"));
        assertEquals("value2", first.get("header2"));

        CSVRecord second = records.get(1);
        assertEquals("value3", second.get("header1"));
        assertEquals("value4", second.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withEmptyList_argumentIsFilled() throws IOException {
        List<CSVRecord> targetList = new ArrayList<>();
        List<CSVRecord> returned = parser.getRecords(targetList);
        assertSame(targetList, returned);
        assertEquals(2, returned.size());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withNonEmptyList_argumentIsFilled() throws IOException {
        List<CSVRecord> targetList = new ArrayList<>();
        targetList.add(mock(CSVRecord.class));
        List<CSVRecord> returned = parser.getRecords(targetList);
        assertSame(targetList, returned);
        assertEquals(3, returned.size());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_handlesIOException() throws Exception {
        CSVParser spyParser = Mockito.spy(parser);
        doThrow(new IOException("Forced IOException")).when(spyParser).nextRecord();

        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        List<CSVRecord> list = new ArrayList<>();
        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                getRecordsMethod.invoke(spyParser, list);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw e;
            }
        });

        assertEquals("Forced IOException", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testNextRecord_privateMethodInvocation() throws Exception {
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(parser);
        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
    }
}