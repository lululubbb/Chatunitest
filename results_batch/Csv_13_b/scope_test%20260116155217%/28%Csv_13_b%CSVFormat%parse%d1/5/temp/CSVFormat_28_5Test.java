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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_5Test {

    private Reader mockReader;

    @BeforeEach
    void setUp() throws Exception {
        mockReader = mock(Reader.class);

        // Prepare mockReader to return something usable by CSVParser
        // CSVParser reads from Reader, so we need to mock read() method.
        when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
            char[] buffer = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            // Return -1 to simulate EOF immediately, so parser doesn't block
            return -1;
        });
    }

    @Test
    @Timeout(8000)
    void testParse_DefaultFormat() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void testParse_CustomFormat() throws IOException {
        CSVFormat format = CSVFormat.newFormat(';')
                .withQuote('\'')
                .withEscape('\\')
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withRecordSeparator("\n")
                .withNullString("NULL")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withCommentMarker('#')
                .withHeader("col1", "col2")
                .withHeaderComments("comment1", "comment2")
                .withQuoteMode(null);
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullReader_ThrowsException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> format.parse(null));
    }

}