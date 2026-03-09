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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_49_4Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        char commentStart = '#';
        char escape = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Character escapeCharacter = '!';
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);

        // When
        CSVFormat result = (CSVFormat) method.invoke(csvFormat, escapeCharacter);

        // Then
        assertNotEquals(csvFormat, result);
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentStart, result.getCommentMarker());
        assertEquals(escapeCharacter, result.getEscapeCharacter());
        assertFalse(result.getIgnoreSurroundingSpaces());
        assertTrue(result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertArrayEquals(headerComments, result.getHeaderComments());
        assertArrayEquals(header, result.getHeader());
        assertFalse(result.getSkipHeaderRecord());
        assertFalse(result.getAllowMissingColumnNames());
        assertFalse(result.getIgnoreHeaderCase());
        assertFalse(result.getTrim());
        assertFalse(result.getTrailingDelimiter());
    }
}