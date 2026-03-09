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

import java.lang.reflect.Method;

class CSVFormat_26_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentStart_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = original.withCommentStart(commentChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentStart());

        // Original should remain unchanged (immutability)
        assertNull(original.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStart_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Access withCommentStart(Character) method via reflection
        Method method = CSVFormat.class.getDeclaredMethod("withCommentStart", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(original, new Object[]{null});

        assertNotNull(result);
        assertNull(result.getCommentStart());

        // Original should remain unchanged (immutability)
        assertNull(original.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStart_Character_nonNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character commentChar = '@';

        // Access withCommentStart(Character) method via reflection
        Method method = CSVFormat.class.getDeclaredMethod("withCommentStart", Character.class);
        method.setAccessible(true);

        CSVFormat result = (CSVFormat) method.invoke(original, commentChar);

        assertNotNull(result);
        assertEquals(commentChar, result.getCommentStart());

        // Original should remain unchanged (immutability)
        assertNull(original.getCommentStart());
    }
}