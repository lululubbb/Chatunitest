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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;

public class CSVFormat_19_5Test {

    @Test
    @Timeout(8000)
    public void testHashCode() {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentMarker = '#';
        Character escapeCharacter = '\\';
        String nullString = "NULL";
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        boolean skipHeaderRecord = true;
        String recordSeparator = "\r\n";
        String[] header = {"Header1", "Header2"};

        CSVFormat csvFormat = Mockito.spy(CSVFormat.DEFAULT);
        Mockito.doReturn(delimiter).when(csvFormat).getDelimiter();
        Mockito.doReturn(quoteCharacter).when(csvFormat).getQuoteCharacter();
        Mockito.doReturn(quoteMode).when(csvFormat).getQuoteMode();
        Mockito.doReturn(commentMarker).when(csvFormat).getCommentMarker();
        Mockito.doReturn(escapeCharacter).when(csvFormat).getEscapeCharacter();
        Mockito.doReturn(nullString).when(csvFormat).getNullString();
        Mockito.doReturn(ignoreSurroundingSpaces).when(csvFormat).getIgnoreSurroundingSpaces();
        Mockito.doReturn(ignoreEmptyLines).when(csvFormat).getIgnoreEmptyLines();
        Mockito.doReturn(skipHeaderRecord).when(csvFormat).getSkipHeaderRecord();
        Mockito.doReturn(recordSeparator).when(csvFormat).getRecordSeparator();
        Mockito.doReturn(header).when(csvFormat).getHeader();

        // When
        int hashCode = csvFormat.hashCode();

        // Then
        int prime = 31;
        int expectedHashCode = 1;
        expectedHashCode = prime * expectedHashCode + delimiter;
        expectedHashCode = prime * expectedHashCode + (quoteMode == null ? 0 : quoteMode.hashCode());
        expectedHashCode = prime * expectedHashCode + (quoteCharacter == null ? 0 : quoteCharacter.hashCode());
        expectedHashCode = prime * expectedHashCode + (commentMarker == null ? 0 : commentMarker.hashCode());
        expectedHashCode = prime * expectedHashCode + (escapeCharacter == null ? 0 : escapeCharacter.hashCode());
        expectedHashCode = prime * expectedHashCode + (nullString == null ? 0 : nullString.hashCode());
        expectedHashCode = prime * expectedHashCode + (ignoreSurroundingSpaces ? 1231 : 1237);
        expectedHashCode = prime * expectedHashCode + (ignoreEmptyLines ? 1231 : 1237);
        expectedHashCode = prime * expectedHashCode + (skipHeaderRecord ? 1231 : 1237);
        expectedHashCode = prime * expectedHashCode + (recordSeparator == null ? 0 : recordSeparator.hashCode());
        expectedHashCode = prime * expectedHashCode + Arrays.deepHashCode(header);

        assertEquals(expectedHashCode, hashCode);
    }
}