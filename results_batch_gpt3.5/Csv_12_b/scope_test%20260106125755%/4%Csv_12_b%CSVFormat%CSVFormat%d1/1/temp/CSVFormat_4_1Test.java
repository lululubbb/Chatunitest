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

public class CSVFormat_4_1Test {

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

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart,
                escape, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator,
                nullString, (Object) header, skipHeaderRecord, allowMissingColumnNames);

        assertEquals(delimiter, format.getDelimiter());
        assertEquals(quoteChar, format.getQuoteCharacter());
        assertEquals(quoteMode, format.getQuoteMode());
        assertEquals(commentStart, format.getCommentMarker());
        assertEquals(escape, format.getEscapeCharacter());
        assertEquals(ignoreSurroundingSpaces, format.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, format.getIgnoreEmptyLines());
        assertEquals(recordSeparator, format.getRecordSeparator());
        assertEquals(nullString, format.getNullString());
        assertArrayEquals(header, format.getHeader());
        assertEquals(skipHeaderRecord, format.getSkipHeaderRecord());
        assertEquals(allowMissingColumnNames, format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_nullHeader() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(',',
                '"', null, null, null,
                false, true, "\r\n",
                null, (Object) null, false, false);

        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_duplicateHeaderThrows() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] duplicateHeader = new String[]{"dup", "dup"};

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance(',',
                    '"', null, null, null,
                    false, true, "\r\n",
                    null, (Object) duplicateHeader, false, false);
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

        char lineBreak = '\n';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            constructor.newInstance(lineBreak,
                    '"', null, null, null,
                    false, true, "\r\n",
                    null, (Object) null, false, false);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructor_validateInvocation() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Use valid parameters to ensure validate() does not throw
        CSVFormat format = constructor.newInstance(',',
                '"', QuoteMode.MINIMAL, '#', '\\',
                true, false, "\r\n",
                "NULL", (Object) new String[]{"a", "b"}, false, false);

        assertNotNull(format);
    }

}