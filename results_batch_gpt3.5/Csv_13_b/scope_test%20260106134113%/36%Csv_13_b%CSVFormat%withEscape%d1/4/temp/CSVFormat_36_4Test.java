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

import org.junit.jupiter.api.Test;

public class CSVFormat_36_4Test {

    @Test
    @Timeout(8000)
    void testWithEscape_withNullEscape() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withEscape((Character) null);
        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        // original format unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withValidEscape() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = '\\';
        CSVFormat result = format.withEscape(escape);
        assertNotNull(result);
        assertEquals(escape, result.getEscapeCharacter());
        // original format unchanged
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withLineBreakEscapeThrows() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Access private static method isLineBreak(char)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Test with '\n' (LF)
        char lf = '\n';
        Boolean lfIsLineBreak = (Boolean) isLineBreakMethod.invoke(null, lf);
        assertTrue(lfIsLineBreak);

        // Test with '\r' (CR)
        char cr = '\r';
        Boolean crIsLineBreak = (Boolean) isLineBreakMethod.invoke(null, cr);
        assertTrue(crIsLineBreak);

        // Test with null (should be false) - call isLineBreak(Character) via reflection
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharMethod.setAccessible(true);
        Boolean nullIsLineBreak = (Boolean) isLineBreakCharMethod.invoke(null, (Character) null);
        assertFalse(nullIsLineBreak);

        // Now test that withEscape throws for line breaks
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(lf);
        });
        assertEquals("The escape character cannot be a line break", thrown1.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            format.withEscape(cr);
        });
        assertEquals("The escape character cannot be a line break", thrown2.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withNonLineBreakEscape() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = 'X';
        // Confirm isLineBreak returns false for 'X'
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
        Boolean isLineBreak = (Boolean) isLineBreakMethod.invoke(null, escape.charValue());
        assertFalse(isLineBreak);

        CSVFormat result = format.withEscape(escape);
        assertNotNull(result);
        assertEquals(escape, result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withPrimitiveCharOverload() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escape = 'Z';
        CSVFormat result = format.withEscape(escape);
        assertNotNull(result);
        assertEquals(Character.valueOf(escape), result.getEscapeCharacter());
    }
}