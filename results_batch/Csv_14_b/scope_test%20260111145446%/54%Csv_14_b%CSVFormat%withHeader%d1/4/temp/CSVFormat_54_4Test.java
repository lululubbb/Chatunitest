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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_54_4Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() {
        // Given
        String[] header = {"Name", "Age", "City"};
        char delimiter = ',';
        char quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeader(header);

        // Then
        assertEquals(delimiter, result.getDelimiter());
        assertEquals(quoteCharacter, result.getQuoteCharacter());
        assertEquals(quoteMode, result.getQuoteMode());
        assertEquals(commentMarker, result.getCommentMarker());
        assertEquals(escapeCharacter, result.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, result.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
        assertEquals(recordSeparator, result.getRecordSeparator());
        assertEquals(nullString, result.getNullString());
        assertArrayEquals(headerComments, result.getHeaderComments());
        assertArrayEquals(header, result.getHeader());
        assertEquals(skipHeaderRecord, result.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
        assertEquals(ignoreHeaderCase, result.getIgnoreHeaderCase());
        assertEquals(trim, result.getTrim());
        assertEquals(trailingDelimiter, result.getTrailingDelimiter());
    }
}