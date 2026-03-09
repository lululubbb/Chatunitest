package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CSVFormat_54_3Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws NoSuchFieldException, IllegalAccessException {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = createCSVFormat('|', '"', null, '#', '\\', true, false, "\r\n",
                "NULL", new Object[]{"Header"}, null, false, true, false, true, false);

        // When
        CSVFormat newCsvFormat = csvFormat.withHeader(header);

        // Then
        assertEquals('|', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
        assertNull(newCsvFormat.getQuoteMode());
        assertEquals('#', newCsvFormat.getCommentMarker());
        assertEquals('\\', newCsvFormat.getEscapeCharacter());
        assertTrue(newCsvFormat.getIgnoreSurroundingSpaces());
        assertFalse(newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals("NULL", newCsvFormat.getNullString());
        assertArrayEquals(new Object[]{"Header"}, newCsvFormat.getHeaderComments());
        assertArrayEquals(header, newCsvFormat.getHeader());
        assertFalse(newCsvFormat.getSkipHeaderRecord());
        assertTrue(newCsvFormat.getAllowMissingColumnNames());
        assertFalse(newCsvFormat.getIgnoreHeaderCase());
        assertTrue(newCsvFormat.getTrim());
        assertFalse(newCsvFormat.getTrailingDelimiter());
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteCharacter, QuoteMode quoteMode, char commentMarker,
                                      char escapeCharacter, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, Object[] headerComments, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames, boolean ignoreHeaderCase,
                                      boolean trim, boolean trailingDelimiter) throws NoSuchFieldException, IllegalAccessException {
        try {
            Field constructor = CSVFormat.class.getDeclaredField("CSVFormat");
            constructor.setAccessible(true);
            return (CSVFormat) constructor.get(CSVFormat.class).newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                    skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}