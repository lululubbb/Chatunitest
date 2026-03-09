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

public class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullObject() {
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
    void testEquals_allFieldsEqual() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter(';');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuotePolicy() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharNullVsNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('"');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar('"');
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('\'');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentStartNullVsNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('#');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentStartDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart('#');
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('!');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeNullVsNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape('\\');
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('/');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNullVsNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString("NULL");
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("null");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentLength() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerDifferentContent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "c");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpacesDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLinesDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecordDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNullVsNonNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorDifferent() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator("\r\n");
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(format1.equals(format2));
    }
}