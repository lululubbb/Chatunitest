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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_2_5Test {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
        Mockito.reset(mockFormat);
    }

    @Test
    @Timeout(8000)
    void parse_NullString_ThrowsNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((String) null, mockFormat));
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> CSVParser.parse("data", null));
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidStringAndFormat_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "header1,header2\nvalue1,value2";
        CSVParser parser = CSVParser.parse(input, mockFormat);
        assertNotNull(parser);
        assertFalse(parser.isClosed());

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(mockFormat, actualFormat);

        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> recordList = (List<String>) recordField.get(parser);
        assertNotNull(recordList);
        assertEquals(0, recordList.size());
    }
}