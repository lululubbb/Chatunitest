package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_15_2Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        Reader mockReader = mock(Reader.class);
        CSVFormat mockFormat = mock(CSVFormat.class);

        // Use the public constructor with Reader and CSVFormat to avoid reflection issues
        Constructor<CSVParser> constructor = CSVParser.class.getConstructor(Reader.class, CSVFormat.class);
        csvParser = constructor.newInstance(mockReader, mockFormat);

        // Use reflection to set required private fields to avoid NullPointerException
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(csvParser, null);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, mock(lexerField.getType()));

        // Spy on csvParser to mock nextRecord method behavior
        csvParser = Mockito.spy(csvParser);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        CSVRecord record3 = mock(CSVRecord.class);

        // nextRecord returns three records then null
        doReturn(record1).doReturn(record2).doReturn(record3).doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertEquals(3, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
        assertSame(record3, records.get(2));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNoRecords() throws Exception {
        // nextRecord returns null immediately
        doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_nextRecordThrowsIOException() throws Exception {
        doThrow(new IOException("IO error")).when(csvParser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> csvParser.getRecords());
        assertEquals("IO error", thrown.getMessage());
    }
}