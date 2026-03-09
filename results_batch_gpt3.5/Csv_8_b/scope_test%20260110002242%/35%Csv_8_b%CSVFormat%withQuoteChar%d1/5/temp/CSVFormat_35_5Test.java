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
import org.junit.jupiter.api.Test;

public class CSVFormat_35_5Test {

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withValidChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = original.withQuoteChar(quoteChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteChar());
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withDoubleQuoteChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '"';

        CSVFormat result = original.withQuoteChar(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteChar());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withNullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        // Use null Character to match withQuoteChar(Character) signature
        Character quoteChar = null;

        CSVFormat result = original.withQuoteChar(quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteChar());
    }
}