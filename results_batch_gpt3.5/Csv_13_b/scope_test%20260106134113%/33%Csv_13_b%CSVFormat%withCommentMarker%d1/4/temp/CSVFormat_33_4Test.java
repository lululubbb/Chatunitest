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

class CSVFormat_33_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';
        CSVFormat updatedFormat = format.withCommentMarker(commentMarker);
        assertNotNull(updatedFormat);
        assertEquals(Character.valueOf(commentMarker), updatedFormat.getCommentMarker());
        // Original format remains unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;
        CSVFormat updatedFormat = format.withCommentMarker(commentMarker);
        assertNotNull(updatedFormat);
        assertNull(updatedFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharCR() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '\r';
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(commentMarker));
        assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharLF() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '\n';
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> format.withCommentMarker(commentMarker));
        assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharCRLF() throws Exception {
        // Use reflection to invoke private static isLineBreak(Character)
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
        Boolean resultCR = (Boolean) isLineBreakMethod.invoke(null, '\r');
        Boolean resultLF = (Boolean) isLineBreakMethod.invoke(null, '\n');
        assertTrue(resultCR);
        assertTrue(resultLF);
    }
}