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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_26_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentStartPrimitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = format.withCommentStart(commentChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentStart());
        // Original instance is immutable, should not be the same
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withCommentStart(Character)
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withCommentStart", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(format, (Object) null);

        assertNotNull(result);
        assertNull(result.getCommentStart());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithCommentStartCharacterObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = Character.valueOf(';');

        // Use reflection to invoke public withCommentStart(Character)
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withCommentStart", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(format, commentChar);

        assertNotNull(result);
        assertEquals(commentChar, result.getCommentStart());
        assertNotSame(format, result);
    }
}