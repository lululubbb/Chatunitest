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

import java.lang.reflect.Method;

public class CSVFormat_33_2Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_ValidCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_NullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_LineBreakCharacter_Throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character[] lineBreakChars = new Character[] { '\n', '\r' };

        for (Character lb : lineBreakChars) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreak_PrivateMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        // Test char version
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        // Test Character version
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, (Character) '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}