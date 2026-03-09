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
    void testWithEscapeValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';
        CSVFormat newFormat = format.withEscape(escapeChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Original format unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeNullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withEscape((Character) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getEscapeCharacter());
        // Original format unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsForLineBreakChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char[] lineBreaks = { '\n', '\r' };
        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> format.withEscape(lb));
            assertEquals("The escape character cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithEscapeThrowsForLineBreakCharacterObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character[] lineBreaks = { '\n', '\r' };
        for (Character lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> format.withEscape(lb));
            assertEquals("The escape character cannot be a line break", ex.getMessage());
        }
    }
}