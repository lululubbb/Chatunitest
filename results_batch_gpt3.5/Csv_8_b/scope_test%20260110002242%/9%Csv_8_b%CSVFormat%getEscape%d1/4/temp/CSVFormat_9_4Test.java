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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

class CSVFormat_9_4Test {

    private CSVFormat defaultFormat;
    private CSVFormat customEscapeFormatNull;
    private CSVFormat customEscapeFormatChar;

    @BeforeEach
    void setUp() throws Exception {
        defaultFormat = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        customEscapeFormatNull = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteChar(),
                null,
                CSVFormat.DEFAULT.getCommentStart(),
                null,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord());

        Character escapeChar = '\\';
        customEscapeFormatChar = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteChar(),
                null,
                CSVFormat.DEFAULT.getCommentStart(),
                escapeChar,
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetEscape_Default() {
        Character escape = defaultFormat.getEscape();
        assertNull(escape, "Default CSVFormat escape should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscape_NullEscape() {
        Character escape = customEscapeFormatNull.getEscape();
        assertNull(escape, "CSVFormat with null escape should return null");
    }

    @Test
    @Timeout(8000)
    void testGetEscape_WithEscapeChar() {
        Character escape = customEscapeFormatChar.getEscape();
        assertNotNull(escape, "CSVFormat with escape char should not return null");
        assertEquals('\\', escape.charValue(), "Escape char should be backslash");
    }
}