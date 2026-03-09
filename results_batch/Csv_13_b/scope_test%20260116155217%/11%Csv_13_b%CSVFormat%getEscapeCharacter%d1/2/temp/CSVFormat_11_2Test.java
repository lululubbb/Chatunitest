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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_11_2Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Default escape character should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character should not be null");
        assertEquals(Character.valueOf('\\'), escapeChar, "Escape character should be backslash");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar_Null() throws Exception {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                null, // escapeCharacter explicitly set to null
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                null,
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase()
        );
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Escape character explicitly set to null should be null");
    }
}