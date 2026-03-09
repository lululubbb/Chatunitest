package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_validEscapeCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat updated = original.withEscape(escapeChar);
        assertNotNull(updated);
        assertEquals(Character.valueOf(escapeChar), updated.getEscapeCharacter());
        // Original should remain unchanged (immutability)
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_nullEscapeCharacter() {
        CSVFormat original = CSVFormat.DEFAULT.withEscape('\\');
        CSVFormat updated = original.withEscape((Character) null);
        assertNotNull(updated);
        assertNull(updated.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_escapeIsLineBreakCR() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = '\r';
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> original.withEscape(escapeChar));
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_escapeIsLineBreakLF() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = '\n';
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> original.withEscape(escapeChar));
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_escapeIsLineBreakNullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\0';
        // \0 is not a line break, should succeed
        CSVFormat updated = original.withEscape(escapeChar);
        assertNotNull(updated);
        assertEquals(Character.valueOf(escapeChar), updated.getEscapeCharacter());
    }
}