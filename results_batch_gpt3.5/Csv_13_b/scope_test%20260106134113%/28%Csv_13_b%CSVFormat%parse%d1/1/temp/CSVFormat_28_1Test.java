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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

class CSVFormat_28_1Test {

    @Mock
    private Reader mockReader;

    private CSVFormat csvFormat;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        csvFormat = CSVFormat.DEFAULT;

        // Mock behavior of Reader to avoid IOException on read
        when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);

        assertEquals(csvFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParseWithNullReaderThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParseDifferentFormats() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format1 = CSVFormat.newFormat(';').withQuote('\'').withIgnoreEmptyLines(true);
        CSVParser parser1 = format1.parse(mockReader);
        assertNotNull(parser1);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parser1Format = (CSVFormat) formatField.get(parser1);
        assertEquals(format1, parser1Format);

        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter('|').withQuote(null);
        CSVParser parser2 = format2.parse(mockReader);
        assertNotNull(parser2);

        CSVFormat parser2Format = (CSVFormat) formatField.get(parser2);
        assertEquals(format2, parser2Format);
    }
}