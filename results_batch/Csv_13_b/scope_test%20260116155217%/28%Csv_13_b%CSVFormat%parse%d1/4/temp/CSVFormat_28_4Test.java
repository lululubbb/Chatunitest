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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_4Test {

    private CSVFormat csvFormat;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to access the private 'format' field in CSVParser
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
}