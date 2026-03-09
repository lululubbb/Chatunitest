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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_36_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() throws NoSuchFieldException, IllegalAccessException {
        // Create a CSVFormat instance for testing
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        // Set up the parameters for the new CSVFormat
        char newDelimiter = ';';
        char newQuoteCharacter = '\'';
        QuoteMode newQuoteMode = QuoteMode.ALL;
        char newCommentMarker = '#';
        char newEscapeCharacter = '\\';
        boolean newIgnoreSurroundingSpaces = true;
        boolean newIgnoreEmptyLines = false;
        String newRecordSeparator = "\n";
        String newNullString = "NULL";
        String[] newHeader = {"Header1", "Header2"};
        boolean newSkipHeaderRecord = true;
        boolean newAllowMissingColumnNames = true;

        // Call the method to test
        CSVFormat newCsvFormat = csvFormat.withIgnoreSurroundingSpaces(newIgnoreSurroundingSpaces);

        // Use reflection to access private fields for comparison
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        assertEquals(newDelimiter, delimiterField.get(newCsvFormat));

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        assertEquals(newQuoteCharacter, quoteCharacterField.get(newCsvFormat));

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        assertEquals(newQuoteMode, quoteModeField.get(newCsvFormat));

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        assertEquals(newCommentMarker, commentMarkerField.get(newCsvFormat));

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        assertEquals(newEscapeCharacter, escapeCharacterField.get(newCsvFormat));

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        assertEquals(newIgnoreSurroundingSpaces, ignoreSurroundingSpacesField.get(newCsvFormat));

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        assertEquals(newIgnoreEmptyLines, ignoreEmptyLinesField.get(newCsvFormat));

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        assertEquals(newRecordSeparator, recordSeparatorField.get(newCsvFormat));

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertEquals(newNullString, nullStringField.get(newCsvFormat));

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertEquals(newHeader, headerField.get(newCsvFormat));

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        assertEquals(newSkipHeaderRecord, skipHeaderRecordField.get(newCsvFormat));

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        assertEquals(newAllowMissingColumnNames, allowMissingColumnNamesField.get(newCsvFormat));
    }
}