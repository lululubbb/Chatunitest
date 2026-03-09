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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_11_4Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() {
        parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withEmptyCollection_returnsEmpty() throws IOException {
        // Arrange
        Collection<CSVRecord> coll = new ArrayList<>();
        // Use doReturn with varargs to avoid calling real method during stubbing
        doReturn(null).when(parser).nextRecord();

        // Act
        Collection<CSVRecord> result = parser.getRecords(coll);

        // Assert
        assertSame(coll, result);
        assertTrue(result.isEmpty());
        verify(parser, times(1)).nextRecord();
    }

    @Test
    @Timeout(8000)
    void testGetRecords_withSomeRecords_addsAllRecords() throws IOException {
        // Arrange
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        Collection<CSVRecord> coll = new ArrayList<>();

        // Chain doReturn calls with varargs to simulate multiple returns
        doReturn(record1, record2, null).when(parser).nextRecord();

        // Act
        Collection<CSVRecord> result = parser.getRecords(coll);

        // Assert
        assertSame(coll, result);
        assertEquals(2, result.size());
        assertTrue(result.contains(record1));
        assertTrue(result.contains(record2));
        verify(parser, times(3)).nextRecord();
    }
}