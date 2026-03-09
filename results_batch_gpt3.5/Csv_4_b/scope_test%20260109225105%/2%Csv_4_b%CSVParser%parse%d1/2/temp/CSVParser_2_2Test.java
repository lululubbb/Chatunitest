package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_2_2Test {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
        // Stub methods on mockFormat to avoid NPE inside CSVParser
        when(mockFormat.getHeader()).thenReturn(null);
        when(mockFormat.getDelimiter()).thenReturn(',');
        // Removed getQuoteCharacter() stub because method does not exist
        when(mockFormat.getRecordSeparator()).thenReturn("\n");
    }

    @Test
    @Timeout(8000)
    void testParse_NullString_ThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, mockFormat);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullFormat_ThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse("data", null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_ValidInput_ReturnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3";
        // Use real CSVFormat instead of mock to avoid NullPointerException inside CSVParser
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(input, format);
        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertNotNull(parser.getHeaderMap());
        assertEquals(0L, parser.getRecordNumber());
    }
}