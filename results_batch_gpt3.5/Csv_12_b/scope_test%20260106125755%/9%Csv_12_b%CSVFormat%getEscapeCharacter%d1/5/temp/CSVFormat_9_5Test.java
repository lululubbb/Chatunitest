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

class CSVFormat_9_5Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_whenEscapeCharacterIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_whenEscapeCharacterIsSet() {
        char escapeChar = '\\';
        CSVFormat format = CSVFormat.DEFAULT.withEscape(escapeChar);
        assertEquals(Character.valueOf(escapeChar), format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_withDifferentEscapeCharacter() {
        char escapeChar = '!';
        CSVFormat format = CSVFormat.DEFAULT.withEscape(escapeChar);
        assertEquals(Character.valueOf(escapeChar), format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_defaultFormat() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_mysqlFormat() throws Exception {
        CSVFormat format = CSVFormat.MYSQL;

        // Use reflection to access the private field escapeCharacter
        java.lang.reflect.Field field = CSVFormat.class.getDeclaredField("escapeCharacter");
        field.setAccessible(true);
        Character escapeChar = (Character) field.get(format);

        assertEquals(Character.valueOf('\\'), escapeChar);
    }
}