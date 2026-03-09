package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormat_30_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_shouldReturnCSVParser() throws IOException {
        Reader mockReader = mock(Reader.class);
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private 'format' field from CSVParser
        CSVFormat parserFormat = getCSVParserFormat(parser);
        assertEquals(csvFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.parse(null);
        });
    }

    @Test
    @Timeout(8000)
    void testParse_withCustomFormat_shouldReturnCSVParser() throws IOException {
        CSVFormat customFormat = CSVFormat.newFormat(';')
                .withQuote('\'')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\n")
                .withTrim(true);

        Reader mockReader = mock(Reader.class);

        CSVParser parser = customFormat.parse(mockReader);
        assertNotNull(parser);

        CSVFormat parserFormat = getCSVParserFormat(parser);
        assertEquals(customFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParse_invokesCSVParserConstructor() throws Exception {
        // Using reflection to test private constructor behavior indirectly
        Reader mockReader = mock(Reader.class);
        CSVParser parser = csvFormat.parse(mockReader);

        assertNotNull(parser);
        // Validate that CSVParser was constructed with correct Reader and CSVFormat
        Reader parserReader = getCSVParserReader(parser);
        CSVFormat parserFormat = getCSVParserFormat(parser);

        assertSame(mockReader, parserReader);
        assertSame(csvFormat, parserFormat);
    }

    // Helper methods to access private fields of CSVParser via reflection

    private CSVFormat getCSVParserFormat(CSVParser parser) {
        try {
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            return (CSVFormat) formatField.get(parser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access 'format' field in CSVParser: " + e.getMessage());
            return null; // unreachable
        }
    }

    private Reader getCSVParserReader(CSVParser parser) {
        try {
            Field readerField = CSVParser.class.getDeclaredField("reader");
            readerField.setAccessible(true);
            return (Reader) readerField.get(parser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access 'reader' field in CSVParser: " + e.getMessage());
            return null; // unreachable
        }
    }
}