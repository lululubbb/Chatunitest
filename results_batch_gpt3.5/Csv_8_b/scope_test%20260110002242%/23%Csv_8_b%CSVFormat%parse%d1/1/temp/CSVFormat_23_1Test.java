package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_23_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader reader = mock(Reader.class);
        CSVParser parser = csvFormat.parse(reader);
        assertNotNull(parser);

        // Using reflection to get private fields 'format' and 'in' from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, formatValue);

        Field readerField = CSVParser.class.getDeclaredField("in");
        readerField.setAccessible(true);
        Reader readerValue = (Reader) readerField.get(parser);
        assertEquals(reader, readerValue);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.parse(null);
        });
    }

    @Test
    @Timeout(8000)
    void testParse_reflectionInvocation() throws Exception {
        Reader reader = mock(Reader.class);
        Method parseMethod = CSVFormat.class.getMethod("parse", Reader.class);
        Object result = parseMethod.invoke(csvFormat, reader);
        assertNotNull(result);
        assertTrue(result instanceof CSVParser);
        CSVParser parser = (CSVParser) result;

        // Using reflection to get private fields 'format' and 'in' from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, formatValue);

        Field readerField = CSVParser.class.getDeclaredField("in");
        readerField.setAccessible(true);
        Reader readerValue = (Reader) readerField.get(parser);
        assertEquals(reader, readerValue);
    }

}