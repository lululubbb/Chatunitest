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

public class CSVFormat_9_6Test {

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWhenEscapeIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWhenEscapeIsSet() {
        char escapeChar = '\\';
        CSVFormat format = CSVFormat.DEFAULT.withEscape(escapeChar);
        assertEquals(Character.valueOf(escapeChar), format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWithDefaultFormat() {
        // DEFAULT has escapeCharacter null
        assertNull(CSVFormat.DEFAULT.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacterWithMYSQLFormat() {
        // MYSQL sets escape to BACKSLASH (which is '\\')
        assertNotNull(CSVFormat.MYSQL.getEscapeCharacter());
        assertEquals(Character.valueOf('\\'), CSVFormat.MYSQL.getEscapeCharacter());
    }
}