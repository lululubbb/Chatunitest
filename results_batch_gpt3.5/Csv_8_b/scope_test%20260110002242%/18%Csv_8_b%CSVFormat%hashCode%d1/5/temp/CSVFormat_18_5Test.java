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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullQuotePolicyAndQuoteChar() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_WithAllFieldsSet() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format = createCSVFormat(
                ';',
                '\"',
                Quote.ALL,
                '#',
                '\\',
                true,
                false,
                "\n",
                "NULL",
                header,
                true);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderEmptyArray() throws Exception {
        String[] header = new String[0];
        CSVFormat format = createCSVFormat(
                ',',
                '\"',
                Quote.MINIMAL,
                null,
                null,
                false,
                false,
                "\r\n",
                null,
                header,
                false);
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_BooleanFieldsVariations() throws Exception {
        String[] header = null;
        CSVFormat format1 = createCSVFormat(
                ',',
                '\"',
                Quote.MINIMAL,
                null,
                null,
                true,
                true,
                "\r\n",
                null,
                header,
                true);
        CSVFormat format2 = createCSVFormat(
                ',',
                '\"',
                Quote.MINIMAL,
                null,
                null,
                false,
                false,
                "\r\n",
                null,
                header,
                false);
        assertNotEquals(format1.hashCode(), format2.hashCode());
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, Quote quotePolicy,
            Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines, String recordSeparator, String nullString,
            String[] header, boolean skipHeaderRecord) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quotePolicy, commentStart,
                escape, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                header, skipHeaderRecord);
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;
        result = prime * result + format.getDelimiter();
        result = prime * result + ((format.getQuotePolicy() == null) ? 0 : format.getQuotePolicy().hashCode());
        result = prime * result + ((format.getQuoteChar() == null) ? 0 : format.getQuoteChar().hashCode());
        result = prime * result + ((format.getCommentStart() == null) ? 0 : format.getCommentStart().hashCode());
        result = prime * result + ((format.getEscape() == null) ? 0 : format.getEscape().hashCode());
        result = prime * result + ((format.getNullString() == null) ? 0 : format.getNullString().hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        result = prime * result + ((format.getRecordSeparator() == null) ? 0 : format.getRecordSeparator().hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());
        return result;
    }
}