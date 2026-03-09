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

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_40_2Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat result = original.withQuoteMode(newQuoteMode);

        assertNotNull(result);
        assertNotSame(original, result);

        // Use reflection to access private fields to verify internal state
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);

        QuoteMode originalQuoteMode = (QuoteMode) quoteModeField.get(original);
        QuoteMode resultQuoteMode = (QuoteMode) quoteModeField.get(result);

        // The original DEFAULT has quoteMode == null, so assertNull is correct
        assertNull(originalQuoteMode);
        assertEquals(newQuoteMode, resultQuoteMode);

        // Verify other fields remain unchanged
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        assertEquals(delimiterField.getChar(original), delimiterField.getChar(result));

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        assertEquals(quoteCharField.get(original), quoteCharField.get(result));

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        assertEquals(commentMarkerField.get(original), commentMarkerField.get(result));

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        assertEquals(escapeCharacterField.get(original), escapeCharacterField.get(result));

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        assertEquals(ignoreSurroundingSpacesField.getBoolean(original), ignoreSurroundingSpacesField.getBoolean(result));

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        assertEquals(ignoreEmptyLinesField.getBoolean(original), ignoreEmptyLinesField.getBoolean(result));

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        assertEquals(recordSeparatorField.get(original), recordSeparatorField.get(result));

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertEquals(nullStringField.get(original), nullStringField.get(result));

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertArrayEquals((String[]) headerField.get(original), (String[]) headerField.get(result));

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        assertEquals(skipHeaderRecordField.getBoolean(original), skipHeaderRecordField.getBoolean(result));

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        assertEquals(allowMissingColumnNamesField.getBoolean(original), allowMissingColumnNamesField.getBoolean(result));
    }
}