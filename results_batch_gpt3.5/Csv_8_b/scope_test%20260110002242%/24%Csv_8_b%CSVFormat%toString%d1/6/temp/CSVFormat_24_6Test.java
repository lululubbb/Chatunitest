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
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormat_24_6Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, Quote quotePolicy,
                                      Character commentStart, Character escape,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    void testToString_allFieldsSet() throws Exception {
        String[] header = {"A", "B"};
        CSVFormat format = createCSVFormat(';', '"', Quote.MINIMAL, '#', '\\',
                true, true, "\n", "NULL", header, true);

        String toStringResult = format.toString();

        String expectedStart = "Delimiter=<;>";
        assertTrue(toStringResult.startsWith(expectedStart));
        assertTrue(toStringResult.contains("Escape=<\\>"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertTrue(toStringResult.contains("CommentStart=<#>"));
        assertTrue(toStringResult.contains("NullString=<NULL>"));
        assertTrue(toStringResult.contains("RecordSeparator=<\n>"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertTrue(toStringResult.contains("SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:true"));
        assertTrue(toStringResult.contains("Header:" + Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    void testToString_noOptionalFields() throws Exception {
        CSVFormat format = createCSVFormat(',', null, null, null, null,
                false, false, null, null, null, false);

        String toStringResult = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", toStringResult);
    }

    @Test
    @Timeout(8000)
    void testToString_someOptionalFields() throws Exception {
        CSVFormat format = createCSVFormat(',', '"', Quote.MINIMAL, null, null,
                false, true, null, null, null, false);

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<,>"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, (Character) '\n'));
        assertTrue((Boolean) method.invoke(null, (Character) '\r'));
        assertFalse((Boolean) method.invoke(null, (Character) 'a'));
        assertFalse((Boolean) method.invoke(null, (Character) null));
    }
}