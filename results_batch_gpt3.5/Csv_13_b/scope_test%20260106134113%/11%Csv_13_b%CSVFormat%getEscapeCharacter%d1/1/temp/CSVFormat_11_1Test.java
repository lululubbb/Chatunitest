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

class CSVFormat_11_1Test {

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
        assertNotNull(escapeChar, "Escape character should not be null");
        assertEquals(Character.valueOf('\\'), escapeChar, "Escape character should be backslash");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar_Null() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Escape character explicitly set to null should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_MySQLFormat() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "MYSQL CSVFormat escapeCharacter should not be null");
        assertEquals(Character.valueOf('\\'), escapeChar, "MYSQL format escape character should be backslash");
    }
}