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
import java.lang.reflect.Method;

class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    void testEquals_sameObject() {
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
    void testEquals_equalObjects() {
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
    void testEquals_differentQuoteChar() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar('\"');
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('\'');

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withQuoteChar((Character) null);
        CSVFormat format2 = CSVFormat.DEFAULT.withQuoteChar('\"');

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentCommentStart() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart('#');
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('!');

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentStartNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withCommentStart((Character) null);
        CSVFormat format2 = CSVFormat.DEFAULT.withCommentStart('!');

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentEscape() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape('\\');
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('/');

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape((Character) null);
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('\\');

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentNullString() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString("null1");
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("null2");

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withNullString(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withNullString("null");

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentHeader() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("A", "B");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("B", "A");

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader((String[]) null);
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("A");

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentIgnoreSurroundingSpaces() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentIgnoreEmptyLines() {
        CSVFormat format1 = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentSkipHeaderRecord() {
        CSVFormat format1 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentRecordSeparator() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\r");

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNull() {
        CSVFormat format1 = CSVFormat.DEFAULT.withRecordSeparator(null);
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\n");

        assertFalse(format1.equals(format2));
        assertFalse(format2.equals(format1));
        assertTrue(format1.equals(format1));
    }

    @Test
    @Timeout(8000)
    void testEquals_reflectionInvoke() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        Method equalsMethod = CSVFormat.class.getDeclaredMethod("equals", Object.class);
        equalsMethod.setAccessible(true);

        assertTrue((Boolean) equalsMethod.invoke(format1, format1));
        assertFalse((Boolean) equalsMethod.invoke(format1, (Object) null));
        assertFalse((Boolean) equalsMethod.invoke(format1, "string"));
    }
}