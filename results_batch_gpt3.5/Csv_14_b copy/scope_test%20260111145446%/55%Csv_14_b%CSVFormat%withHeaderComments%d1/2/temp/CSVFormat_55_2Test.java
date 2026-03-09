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

public class CSVFormat_55_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        // Given
        Object[] headerComments = {"Comment 1", "Comment 2"};
        char delimiter = ',';
        char quoteCharacter = '"';
        QuoteMode quoteMode = QuoteMode.NON_NUMERIC;
        Character commentMarker = '#';
        Character escapeCharacter = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = "NULL";
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = true;
        boolean trim = true;
        boolean trailingDelimiter = false;

        CSVFormat csvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteCharacter)
                .withQuoteMode(quoteMode)
                .withCommentMarker(commentMarker)
                .withEscape(escapeCharacter)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator)
                .withNullString(nullString)
                .withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord)
                .withAllowMissingColumnNames(allowMissingColumnNames)
                .withIgnoreHeaderCase(ignoreHeaderCase)
                .withTrim(trim)
                .withTrailingDelimiter(trailingDelimiter);

        CSVFormat expectedFormat = CSVFormat.newFormat(delimiter)
                .withQuote(quoteCharacter)
                .withQuoteMode(quoteMode)
                .withCommentMarker(commentMarker)
                .withEscape(escapeCharacter)
                .withIgnoreSurroundingSpaces(ignoreSurroundingSpaces)
                .withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator)
                .withNullString(nullString)
                .withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord)
                .withAllowMissingColumnNames(allowMissingColumnNames)
                .withIgnoreHeaderCase(ignoreHeaderCase)
                .withTrim(trim)
                .withTrailingDelimiter(trailingDelimiter);

        // When
        CSVFormat result = csvFormat.withHeaderComments(headerComments);

        // Then
        assertAll(
                () -> assertEquals(expectedFormat.getDelimiter(), result.getDelimiter()),
                () -> assertEquals(expectedFormat.getQuoteCharacter(), result.getQuoteCharacter()),
                () -> assertEquals(expectedFormat.getQuoteMode(), result.getQuoteMode()),
                () -> assertEquals(expectedFormat.getCommentMarker(), result.getCommentMarker()),
                () -> assertEquals(expectedFormat.getEscapeCharacter(), result.getEscapeCharacter()),
                () -> assertEquals(expectedFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces()),
                () -> assertEquals(expectedFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines()),
                () -> assertEquals(expectedFormat.getRecordSeparator(), result.getRecordSeparator()),
                () -> assertEquals(expectedFormat.getNullString(), result.getNullString()),
                () -> assertArrayEquals(expectedFormat.getHeader(), result.getHeader()),
                () -> assertArrayEquals(expectedFormat.getHeaderComments(), result.getHeaderComments()),
                () -> assertEquals(expectedFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord()),
                () -> assertEquals(expectedFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames()),
                () -> assertEquals(expectedFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase()),
                () -> assertEquals(expectedFormat.getTrim(), result.getTrim()),
                () -> assertEquals(expectedFormat.getTrailingDelimiter(), result.getTrailingDelimiter())
        );
    }
}