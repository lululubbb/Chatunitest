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
    void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_null() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter('|');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuotePolicy() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL);
        CSVFormat format2 = CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteChar_nullAndNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('"');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteChar_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar('"');
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('"');
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentStart_nullAndNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('#');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentStart_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart('#');
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('#');
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escape_nullAndNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_escape_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape('\\');
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_nullAndNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullString_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString("NULL");
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("NULL");
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_different() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("A", "B");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("A", "C");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_header_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("A", "B");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("A", "B");
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpaces_different() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpaces_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLines_different() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLines_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecord_different() {
        CSVFormat format1 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecord_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_nullAndNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparator_equal() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() {
        CSVFormat format1 = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withNullString("null")
                .withHeader("A", "B")
                .withSkipHeaderRecord(true);

        CSVFormat format2 = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withQuoteChar('"')
                .withQuotePolicy(Quote.MINIMAL)
                .withCommentStart('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withNullString("null")
                .withHeader("A", "B")
                .withSkipHeaderRecord(true);

        assertTrue(format1.equals(format2));
    }
}