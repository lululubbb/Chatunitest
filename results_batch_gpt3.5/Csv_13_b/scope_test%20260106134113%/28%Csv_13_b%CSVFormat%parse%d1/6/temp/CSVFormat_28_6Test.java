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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_6Test {

    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testParse_WithValidReader_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);

        // Use getDeclaredField on CSVParser.class to access private fields "in" and "format"
        Field inputField = CSVParser.class.getDeclaredField("in");
        inputField.setAccessible(true);
        Object inputValue = inputField.get(parser);
        assertEquals(mockReader, inputValue);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(parser);
        assertEquals(format, formatValue);
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullReader_ThrowsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> format.parse(null));
    }
}