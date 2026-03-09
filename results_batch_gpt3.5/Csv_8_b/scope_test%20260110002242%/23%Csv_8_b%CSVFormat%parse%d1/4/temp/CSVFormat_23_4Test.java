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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVFormat_23_4Test {

    @Mock
    private Reader mockReader;

    private CSVFormat csvFormat;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        csvFormat = CSVFormat.DEFAULT;
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private 'input' field from CSVParser
        Field readerField = CSVParser.class.getDeclaredField("input");
        readerField.setAccessible(true);
        Reader actualReader = (Reader) readerField.get(parser);
        assertEquals(mockReader, actualReader);

        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    void testParseWithNullReaderThrowsException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParseWithCustomFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat customFormat = CSVFormat.newFormat(';')
                .withQuoteChar('\'')
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n");
        CSVParser parser = customFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private 'input' field from CSVParser
        Field readerField = CSVParser.class.getDeclaredField("input");
        readerField.setAccessible(true);
        Reader actualReader = (Reader) readerField.get(parser);
        assertEquals(mockReader, actualReader);

        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(customFormat, actualFormat);
    }
}