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

import java.lang.reflect.Method;

class CSVFormat_66_6Test {

    @Test
    @Timeout(8000)
    void testWithQuote_NewQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char newQuote = '\'';

        CSVFormat newFormat = format.withQuote(newQuote);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(newQuote), newFormat.getQuoteCharacter());
        // Original format unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_NullQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withQuote((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        // Original format unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_ThrowsOnLineBreakChar() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreaks = { '\n', '\r' };

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> format.withQuote(lb));
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_PrivateStaticCharMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // Line break chars return true
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));

        // Non line break chars return false
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    void testIsLineBreak_PrivateStaticCharacterMethod() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Line break chars return true
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));

        // Null returns false (assuming implementation)
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));

        // Non line break chars return false
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf(',')));
    }
}