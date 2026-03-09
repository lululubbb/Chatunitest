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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class CSVFormat_14_1Test {

    @Test
    @Timeout(8000)
    public void testGetNullString() {
        // Given
        CSVFormat csvFormat = createCSVFormat(CSVFormat.DEFAULT.getDelimiter(), CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(), CSVFormat.DEFAULT.getCommentMarker(), CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(), CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(), "testNullString", CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(), CSVFormat.DEFAULT.getAllowMissingColumnNames());

        // When
        String nullString = (String) invokePrivateMethod(csvFormat, "getNullString");

        // Then
        assertEquals("testNullString", nullString);
    }

    // Helper method to create a CSVFormat object
    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        return CSVFormat.newFormat(delimiter).withQuote(quoteChar).withQuoteMode(quoteMode).withCommentMarker(commentStart)
                .withEscape(escape).withIgnoreSurroundingSpaces(ignoreSurroundingSpaces).withIgnoreEmptyLines(ignoreEmptyLines)
                .withRecordSeparator(recordSeparator).withNullString(nullString).withHeader(header)
                .withSkipHeaderRecord(skipHeaderRecord).withAllowMissingColumnNames(allowMissingColumnNames);
    }

    // Helper method to invoke private method using reflection
    private Object invokePrivateMethod(Object obj, String methodName) {
        try {
            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}