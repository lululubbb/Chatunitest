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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_validParameters() throws Exception {
        // Using reflection to access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '\"';
        Quote quotePolicy = null;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] {"a", "b"};
        boolean skipHeaderRecord = true;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);

        // Validate fields via reflection
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        assertEquals(delimiter, delimiterField.getChar(format));

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        assertEquals(quoteChar, quoteCharField.get(format));

        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        assertEquals(quotePolicy, quotePolicyField.get(format));

        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        assertEquals(commentStart, commentStartField.get(format));

        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        assertEquals(escape, escapeField.get(format));

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        assertEquals(ignoreSurroundingSpaces, ignoreSurroundingSpacesField.getBoolean(format));

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        assertEquals(ignoreEmptyLines, ignoreEmptyLinesField.getBoolean(format));

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        assertEquals(recordSeparator, recordSeparatorField.get(format));

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertEquals(nullString, nullStringField.get(format));

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] actualHeader = (String[]) headerField.get(format);
        assertArrayEquals(header, actualHeader);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        assertEquals(skipHeaderRecord, skipHeaderRecordField.getBoolean(format));
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_headerNull() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, (String[]) null, false);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertNull(headerField.get(format));
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_delimiterLineBreak_throws() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char[] lineBreaks = new char[] {'\r', '\n'};
        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                constructor.newInstance(lb, null, null, null, null,
                        false, true, "\r\n", null, (String[]) null, false);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }
}