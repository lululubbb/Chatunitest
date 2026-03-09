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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_2_4Test {

    @Test
    @Timeout(8000)
    void testIsLineBreakWithNullCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Since char is primitive, cannot pass null directly.
        // So we test that passing '\0' (null character) returns false.
        boolean result = (boolean) isLineBreakMethod.invoke(null, (Object) Character.valueOf('\0'));
        assertFalse(result, "isLineBreak should return false for null character '\\0'");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithLineBreakCharacters() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Characters that should be recognized as line breaks (CR, LF)
        char[] lineBreakChars = { '\r', '\n' };

        for (char c : lineBreakChars) {
            boolean result = (boolean) isLineBreakMethod.invoke(null, (Object) Character.valueOf(c));
            assertTrue(result, "isLineBreak should return true for line break character: " + (int)c);
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithNonLineBreakCharacters() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // Characters that should NOT be recognized as line breaks
        char[] nonLineBreakChars = { 'a', ' ', ',', '\t', '1', '\"' };

        for (char c : nonLineBreakChars) {
            boolean result = (boolean) isLineBreakMethod.invoke(null, (Object) Character.valueOf(c));
            assertFalse(result, "isLineBreak should return false for non-line break character: " + c);
        }
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithCharacterWrapper() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // Test passing null Character returns false
        Boolean resultNull = (Boolean) isLineBreakMethod.invoke(null, new Object[] { null });
        assertFalse(resultNull, "isLineBreak(Character) should return false for null");

        // Characters that should be recognized as line breaks (CR, LF)
        Character[] lineBreakChars = { '\r', '\n' };

        for (Character c : lineBreakChars) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertTrue(result, "isLineBreak(Character) should return true for line break character: " + (int)c.charValue());
        }

        // Characters that should NOT be recognized as line breaks
        Character[] nonLineBreakChars = { 'a', ' ', ',', '\t', '1', '\"' };

        for (Character c : nonLineBreakChars) {
            Boolean result = (Boolean) isLineBreakMethod.invoke(null, c);
            assertFalse(result, "isLineBreak(Character) should return false for non-line break character: " + c);
        }
    }
}