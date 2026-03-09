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
import org.junit.jupiter.api.Test;

class CSVFormatEqualsTest {

    @Test
    @Timeout(8000)
    public void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_allFieldsEqual() {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = CSVFormat.DEFAULT;
        assertTrue(f1.equals(f2));
        assertTrue(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = f1.withDelimiter('|');
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = f1.withQuoteMode(QuoteMode.ALL);
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withQuote(null);
        CSVFormat f2 = CSVFormat.DEFAULT;
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_differentNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withQuote('\'');
        CSVFormat f2 = CSVFormat.DEFAULT.withQuote('\"');
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        CSVFormat f2 = CSVFormat.DEFAULT.withCommentMarker('#');
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_differentNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withCommentMarker('#');
        CSVFormat f2 = CSVFormat.DEFAULT.withCommentMarker('!');
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withEscape((Character) null);
        CSVFormat f2 = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_differentNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withEscape('\\');
        CSVFormat f2 = CSVFormat.DEFAULT.withEscape('/');
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withNullString(null);
        CSVFormat f2 = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_differentNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withNullString("NULL1");
        CSVFormat f2 = CSVFormat.DEFAULT.withNullString("NULL2");
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_differentLength() {
        CSVFormat f1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat f2 = CSVFormat.DEFAULT.withHeader("a");
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_differentContent() {
        CSVFormat f1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat f2 = CSVFormat.DEFAULT.withHeader("a", "c");
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces_different() {
        CSVFormat f1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat f2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines_different() {
        CSVFormat f1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat f2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord_different() {
        CSVFormat f1 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat f2 = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(f1.equals(f2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        CSVFormat f2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(f1.equals(f2));
        assertFalse(f2.equals(f1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_differentNonNull() {
        CSVFormat f1 = CSVFormat.DEFAULT.withRecordSeparator("\r\n");
        CSVFormat f2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(f1.equals(f2));
    }
}