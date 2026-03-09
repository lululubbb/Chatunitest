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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_9_1Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Default CSVFormat escapeCharacter should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character should not be null when set");
        assertEquals(Character.valueOf('\\'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar_Null() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Escape character should be null when explicitly set to null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_MySQLFormat() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar);
        assertEquals(Character.valueOf('\\'), escapeChar);
    }
}