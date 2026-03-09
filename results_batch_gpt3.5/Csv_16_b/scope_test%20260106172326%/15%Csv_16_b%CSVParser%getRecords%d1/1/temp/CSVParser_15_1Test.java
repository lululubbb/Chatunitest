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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVParser_15_1Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy Reader and CSVFormat to allow mocking nextRecord()
        Reader dummyReader = mock(Reader.class);
        CSVFormat dummyFormat = mock(CSVFormat.class);
        parser = Mockito.spy(new CSVParser(dummyReader, dummyFormat));

        // Reset recordNumber and other fields if needed via reflection
        setField(parser, "recordNumber", 0L);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_multipleRecords() throws IOException, Exception {
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        final CSVRecord[] records = new CSVRecord[] {rec1, rec2, rec3, null};
        final int[] index = {0};

        // Override the spy's nextRecord method with a custom Answer that returns our records in sequence
        doAnswer(invocation -> records[index[0]++]).when(parser).nextRecord();

        List<CSVRecord> recordsList = parser.getRecords();

        assertNotNull(recordsList);
        assertEquals(3, recordsList.size());
        assertSame(rec1, recordsList.get(0));
        assertSame(rec2, recordsList.get(1));
        assertSame(rec3, recordsList.get(2));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_noRecords() throws IOException {
        doReturn(null).when(parser).nextRecord();

        List<CSVRecord> records = parser.getRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    // Utility method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = null;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testNextRecordViaReflection() throws Exception {
        // Invoke package-private nextRecord() via reflection
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        Object result = nextRecordMethod.invoke(parser);
        assertNull(result);
    }
}