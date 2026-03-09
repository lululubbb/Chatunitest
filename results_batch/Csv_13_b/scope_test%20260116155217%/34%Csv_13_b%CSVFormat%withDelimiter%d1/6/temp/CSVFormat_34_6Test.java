package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_34_6Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter_returnsNewInstance() throws Exception {
        // Arrange
        char newDelimiter = ';';
        CSVFormat original = createCSVFormatInstance(',');

        // Act
        CSVFormat modified = original.withDelimiter(newDelimiter);

        // Assert
        assertNotNull(modified);
        assertNotSame(original, modified);
        assertEquals(newDelimiter, modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), modified.getRecordSeparator());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiter_throwsIllegalArgumentException() throws Exception {
        CSVFormat original = createCSVFormatInstance(',');

        char[] lineBreakChars = {'\n', '\r'};

        for (char c : lineBreakChars) {
            Executable executable = () -> original.withDelimiter(c);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
            assertEquals("The delimiter cannot be a line break", exception.getMessage());
        }
    }

    // Helper method to create CSVFormat instance via reflection
    private CSVFormat createCSVFormatInstance(char delimiter) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat def = CSVFormat.DEFAULT;

        Object[] headerComments = def.getHeaderComments();
        if (headerComments == null) {
            headerComments = new Object[0];
        }
        String[] header = def.getHeader();
        if (header == null) {
            header = new String[0];
        }

        return constructor.newInstance(
                delimiter,                                  // delimiter
                def.getQuoteCharacter(),                    // quoteChar
                def.getQuoteMode(),                         // quoteMode
                def.getCommentMarker(),                     // commentStart
                def.getEscapeCharacter(),                   // escape
                def.getIgnoreSurroundingSpaces(),           // ignoreSurroundingSpaces
                def.getIgnoreEmptyLines(),                  // ignoreEmptyLines
                def.getRecordSeparator(),                   // recordSeparator
                def.getNullString(),                        // nullString
                headerComments,                             // headerComments (Object[])
                header,                                    // header (String[])
                def.getSkipHeaderRecord(),                  // skipHeaderRecord
                def.getAllowMissingColumnNames(),           // allowMissingColumnNames
                def.getIgnoreHeaderCase()                   // ignoreHeaderCase
        );
    }
}