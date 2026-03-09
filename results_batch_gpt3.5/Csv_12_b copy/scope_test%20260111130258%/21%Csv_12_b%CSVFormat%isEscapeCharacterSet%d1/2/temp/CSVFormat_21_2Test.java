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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.StringWriter;

public class CSVFormat_21_2Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withIgnoreEmptyLines(false);

        // When
        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        // Then
        assertTrue(isEscapeCharacterSet);
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withIgnoreEmptyLines(false)
                .withEscape(null);

        // When
        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        // Then
        assertFalse(isEscapeCharacterSet);
    }

    // Additional test cases can be added for better coverage

}