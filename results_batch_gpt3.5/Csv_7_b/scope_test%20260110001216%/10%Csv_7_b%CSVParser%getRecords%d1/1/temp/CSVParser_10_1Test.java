package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_10_1Test {

    private CSVParser csvParser;
    private CSVFormat formatMock;
    private Reader readerMock;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
        lexerMock = mock(Lexer.class);

        // Create CSVParser instance with mocks, bypassing constructor complexity
        csvParser = Mockito.spy(new CSVParser(readerMock, formatMock));

        // Inject mocks and fields via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        // headerMap is final, so remove final modifier before setting null
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerMapField, headerMapField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        headerMapField.set(csvParser, null); // simulate no header map

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.set(csvParser, 0L);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_ReturnsEmptyListWhenNoRecords() throws IOException {
        // Prepare the nextRecord to return null to simulate no records
        doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_ReturnsListWithRecords() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        // Use spy to mock nextRecord() to return two records then null
        doReturn(record1).doReturn(record2).doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> records = csvParser.getRecords();

        assertNotNull(records);
        assertEquals(2, records.size());
        assertSame(record1, records.get(0));
        assertSame(record2, records.get(1));
    }

    @Test
    @Timeout(8000)
    void testGetRecords_ThrowsIOException() throws Exception {
        // nextRecord throws IOException
        doThrow(new IOException("Test IO Exception")).when(csvParser).nextRecord();

        IOException thrown = assertThrows(IOException.class, () -> csvParser.getRecords());
        assertEquals("Test IO Exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGetRecords_WithExistingList() throws Exception {
        CSVRecord record1 = mock(CSVRecord.class);

        doReturn(record1).doReturn(null).when(csvParser).nextRecord();

        List<CSVRecord> preExistingList = new ArrayList<>();

        // Use reflection to invoke private generic getRecords(List<T>) method
        Method getRecordsMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsMethod.invoke(csvParser, preExistingList);

        assertSame(preExistingList, result);
        assertEquals(1, result.size());
        assertSame(record1, result.get(0));
    }
}