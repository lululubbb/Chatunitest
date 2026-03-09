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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithEscapeChar() {
        char escapeChar = '\\';
        CSVFormat result = csvFormat.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Original instance should be unchanged (immutability)
        assertNull(csvFormat.getEscapeCharacter());
        // The returned instance should not be the same as original
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharDifferent() {
        char escapeChar = 'X';
        CSVFormat result = csvFormat.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertNull(csvFormat.getEscapeCharacter());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharUsingReflection() throws Exception {
        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(csvFormat, Character.valueOf('E'));
        assertNotNull(result);
        assertEquals(Character.valueOf('E'), result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterNull() {
        CSVFormat result = csvFormat.withEscape((Character) null);
        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterSameAsCurrent() {
        CSVFormat formatWithEscape = csvFormat.withEscape('Z');
        CSVFormat result = formatWithEscape.withEscape('Z');
        // Should return the same instance if escape character is unchanged
        assertSame(formatWithEscape, result);
    }

}