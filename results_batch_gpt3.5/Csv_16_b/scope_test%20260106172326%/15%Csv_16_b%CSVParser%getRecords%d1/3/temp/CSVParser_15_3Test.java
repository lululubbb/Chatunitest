package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVParser_15_3Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        Reader reader = new StringReader("");
        CSVFormat format = CSVFormat.DEFAULT;
        // Use a real parser instance instead of spy to avoid issues with private final methods
        parser = new CSVParser(reader, format);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (parser != null && !parser.isClosed()) {
            parser.close();
        }
    }

    @Test
    @Timeout(8000)
    void testGetRecords_empty() throws Exception {
        // Use reflection to mock nextRecord method behavior by subclassing CSVParser
        CSVParser spyParser = spy(parser);
        doReturn(null).when(spyParser).nextRecord();

        List<CSVRecord> records = spyParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
        verify(spyParser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_multipleRecords() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> record1)
                .doAnswer(invocation -> record2)
                .doReturn(null)
                .when(spyParser).nextRecord();

        List<CSVRecord> records = spyParser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        verify(spyParser, atLeast(3)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_privateNextRecordViaReflection() throws Exception {
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // Because nextRecord() is private and final, Mockito cannot mock it directly.
        // Instead, we invoke it directly on a fresh parser with empty input.
        Reader reader = new StringReader("");
        CSVParser freshParser = new CSVParser(reader, CSVFormat.DEFAULT);

        Object result = nextRecordMethod.invoke(freshParser);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_internalStateAfterCall() throws Exception {
        CSVRecord record = mock(CSVRecord.class);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> record)
                .doReturn(null)
                .when(spyParser).nextRecord();

        List<CSVRecord> records = spyParser.getRecords();

        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<?> recordList = (List<?>) recordListField.get(spyParser);

        assertNotNull(recordList);

        assertEquals(1, records.size());
        assertSame(record, records.get(0));
    }
}