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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_3Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to get the constructor with Reader and CSVFormat
        Constructor<CSVParser> ctor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        ctor.setAccessible(true);

        // Create real instance with null arguments (allowed for test)
        CSVParser realParser = ctor.newInstance((java.io.Reader) null, (CSVFormat) null);

        // Create a spy to call real methods but allow stubbing
        parser = Mockito.spy(realParser);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyRecordsAndNoRecordsReturned() throws IOException {
        // Arrange
        doReturn(null).when(parser).nextRecord();
        List<CSVRecord> records = new ArrayList<>();

        // Act
        List<CSVRecord> result = parser.getRecords(records);

        // Assert
        assertSame(records, result);
        assertTrue(result.isEmpty());
        verify(parser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withMultipleRecords() throws IOException {
        // Arrange
        CSVRecord rec1 = mock(CSVRecord.class);
        CSVRecord rec2 = mock(CSVRecord.class);
        CSVRecord rec3 = mock(CSVRecord.class);

        doReturn(rec1).doReturn(rec2).doReturn(rec3).doReturn(null).when(parser).nextRecord();
        List<CSVRecord> records = new ArrayList<>();

        // Act
        List<CSVRecord> result = parser.getRecords(records);

        // Assert
        assertSame(records, result);
        assertEquals(3, result.size());
        assertEquals(rec1, result.get(0));
        assertEquals(rec2, result.get(1));
        assertEquals(rec3, result.get(2));
        verify(parser, atLeastOnce()).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withNullCollection() throws IOException {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> parser.getRecords(null));
    }
}