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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CSVFormat_55_3Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        // Given
        Object[] headerComments = {"Comment1", "Comment2"};
        char delimiter = ',';
        char quoteCharacter = '"';
        QuoteMode quoteMode = QuoteMode.ALL_NON_NULL;
        Character commentMarker = '#';
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = true;
        boolean trim = false;
        boolean trailingDelimiter = true;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeaderComments(headerComments);

        // Then
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertEquals(Arrays.toString(csvFormat.getHeaderComments()), Arrays.toString(result.getHeaderComments()));
        assertEquals(Arrays.toString(csvFormat.getHeader()), Arrays.toString(result.getHeader()));
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(csvFormat.getTrim(), result.getTrim());
        assertEquals(csvFormat.getTrailingDelimiter(), result.getTrailingDelimiter());
    }
}