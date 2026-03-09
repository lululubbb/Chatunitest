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
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_5_6Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, Quote quotePolicy,
                                      Character commentStart, Character escape,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        // Use reflection to invoke private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        return clazz.getDeclaredConstructor(char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class)
                .newInstance(delimiter, quoteChar, quotePolicy, commentStart, escape,
                        ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

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
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = createCSVFormat((char)(format1.getDelimiter() + 1), format1.getQuoteChar(), format1.getQuotePolicy(),
                format1.getCommentStart(), format1.getEscape(), format1.getIgnoreSurroundingSpaces(),
                format1.getIgnoreEmptyLines(), format1.getRecordSeparator(), format1.getNullString(),
                format1.getHeader(), format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuotePolicy() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        Quote differentQuotePolicy = format1.getQuotePolicy() == null ? Quote.ALL : null;
        CSVFormat format2 = createCSVFormat(format1.getDelimiter(), format1.getQuoteChar(), differentQuotePolicy,
                format1.getCommentStart(), format1.getEscape(), format1.getIgnoreSurroundingSpaces(),
                format1.getIgnoreEmptyLines(), format1.getRecordSeparator(), format1.getNullString(),
                format1.getHeader(), format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteChar_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', null, Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', null, Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format4 = createCSVFormat(',', null, Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentStart_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, '#', null,
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format4 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escape_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, null, '\\',
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format4 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", "NULL", null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        CSVFormat format4 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_arraysDifferent() throws Exception {
        String[] header1 = new String[] {"a", "b"};
        String[] header2 = new String[] {"a", "c"};
        CSVFormat format1 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, header1, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, header2, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, header1, false);
        CSVFormat format4 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, header1.clone(), false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_booleanFields() throws Exception {
        CSVFormat base = CSVFormat.DEFAULT;

        CSVFormat diffIgnoreSurroundingSpaces = createCSVFormat(base.getDelimiter(), base.getQuoteChar(), base.getQuotePolicy(),
                base.getCommentStart(), base.getEscape(), !base.getIgnoreSurroundingSpaces(),
                base.getIgnoreEmptyLines(), base.getRecordSeparator(), base.getNullString(),
                base.getHeader(), base.getSkipHeaderRecord());
        assertFalse(base.equals(diffIgnoreSurroundingSpaces));

        CSVFormat diffIgnoreEmptyLines = createCSVFormat(base.getDelimiter(), base.getQuoteChar(), base.getQuotePolicy(),
                base.getCommentStart(), base.getEscape(), base.getIgnoreSurroundingSpaces(),
                !base.getIgnoreEmptyLines(), base.getRecordSeparator(), base.getNullString(),
                base.getHeader(), base.getSkipHeaderRecord());
        assertFalse(base.equals(diffIgnoreEmptyLines));

        CSVFormat diffSkipHeaderRecord = createCSVFormat(base.getDelimiter(), base.getQuoteChar(), base.getQuotePolicy(),
                base.getCommentStart(), base.getEscape(), base.getIgnoreSurroundingSpaces(),
                base.getIgnoreEmptyLines(), base.getRecordSeparator(), base.getNullString(),
                base.getHeader(), !base.getSkipHeaderRecord());
        assertFalse(base.equals(diffSkipHeaderRecord));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, null, null, null, false);
        CSVFormat format2 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, "\n", null, null, false);
        assertFalse(format1.equals(format2));

        CSVFormat format3 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, null, null, null, false);
        CSVFormat format4 = createCSVFormat(',', '\"', Quote.ALL, null, null,
                false, false, null, null, null, false);
        assertTrue(format3.equals(format4));
    }

    @Test
    @Timeout(8000)
    public void testEquals_allFieldsEqual() throws Exception {
        String[] header = new String[] {"col1", "col2"};
        CSVFormat format1 = createCSVFormat(';', '\"', Quote.ALL, '#', '\\',
                true, true, "\r\n", "NULL", header, true);
        CSVFormat format2 = createCSVFormat(';', '\"', Quote.ALL, '#', '\\',
                true, true, "\r\n", "NULL", header.clone(), true);
        assertTrue(format1.equals(format2));
    }
}