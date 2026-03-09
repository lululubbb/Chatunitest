package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
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
    void testWithEscapeValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat newFormat = format.withEscape(escapeChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Original format should remain unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeNullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = null;
        CSVFormat newFormat = format.withEscape(escapeChar);
        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
        // Original format should remain unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsOnLineBreakLf() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\n'; // LF
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsOnLineBreakCr() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\r'; // CR
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(escapeChar);
        });
        assertEquals("The escape character cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsOnLineBreakNullChar() throws Exception {
        // Using reflection to invoke private static isLineBreak(Character)
        java.lang.reflect.Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, (Character) null));
    }
}