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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CSVFormat_4_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_validParameters() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = QuoteMode.ALL;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[]{"col1", "col2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;

        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart,
                escape, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, header, skipHeaderRecord, allowMissingColumnNames);

        assertEquals(delimiter, csvFormat.getDelimiter());
        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
        assertEquals(quoteMode, csvFormat.getQuoteMode());
        assertEquals(commentStart, csvFormat.getCommentMarker());
        assertEquals(escape, csvFormat.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, csvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, csvFormat.getIgnoreEmptyLines());
        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
        assertEquals(nullString, csvFormat.getNullString());
        assertArrayEquals(header, csvFormat.getHeader());
        assertEquals(skipHeaderRecord, csvFormat.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, csvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullHeader() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', null, null, null,
                null, false, true, "\r\n",
                null, (Object) null, false, false);

        assertNull(csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_duplicateHeaderThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] header = new String[]{"col1", "col2", "col1"};

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance(',', null, null, null,
                    null, false, true, "\r\n",
                    null, header, false, false);
        });
        assertTrue(thrown.getMessage().contains("duplicate entry"));
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_invalidDelimiterLineBreakThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Use '\n' as delimiter which is a line break - should throw
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance('\n', null, null, null,
                    null, false, true, "\r\n",
                    null, (Object) null, false, false);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) ','));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}