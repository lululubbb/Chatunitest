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

public class CSVFormat_33_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_ValidChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(commentMarker, newFormat.getCommentMarker());

        // Ensure original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_NullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());

        // Ensure original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_LineBreakCharThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character[] lineBreaks = new Character[] { '\n', '\r' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakPrivateStaticMethod() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test line break characters
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r')));

        // Test non-line break characters
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('#')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_CharOverload_ValidChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';

        CSVFormat newFormat = format.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());

        // Ensure original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_CharOverload_LineBreakThrows() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreaks = new char[] { '\n', '\r' };

        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}