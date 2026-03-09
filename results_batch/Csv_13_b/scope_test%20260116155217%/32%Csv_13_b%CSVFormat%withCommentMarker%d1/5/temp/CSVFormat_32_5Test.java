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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_32_5Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = original.withCommentMarker(commentChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(original, new Object[] { null });

        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Character_nonNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character commentChar = Character.valueOf('!');

        Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(original, commentChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(commentChar, result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_char_edgeCases() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with line break character '\n'
        char lf = '\n';
        CSVFormat resultLf = original.withCommentMarker(lf);
        assertEquals(Character.valueOf(lf), resultLf.getCommentMarker());

        // Test with tab character '\t'
        char tab = '\t';
        CSVFormat resultTab = original.withCommentMarker(tab);
        assertEquals(Character.valueOf(tab), resultTab.getCommentMarker());

        // Test with backslash character '\\'
        char backslash = '\\';
        CSVFormat resultBackslash = original.withCommentMarker(backslash);
        assertEquals(Character.valueOf(backslash), resultBackslash.getCommentMarker());
    }
}