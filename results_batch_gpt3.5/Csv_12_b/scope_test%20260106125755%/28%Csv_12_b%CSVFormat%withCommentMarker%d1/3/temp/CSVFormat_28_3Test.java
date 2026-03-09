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

class CSVFormat_28_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_overload() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        // Call the public withCommentMarker(char) method
        CSVFormat result = original.withCommentMarker(commentChar);

        // The result should not be the same instance (immutable pattern)
        assertNotSame(original, result);

        // The comment marker in result should be equal to Character.valueOf(commentChar)
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());

        // Also test that original is unchanged
        assertNull(original.getCommentMarker());

        // Using reflection to invoke private withCommentMarker(Character)
        Method privateWithCommentMarker = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        privateWithCommentMarker.setAccessible(true);

        // Invoke with a Character argument
        CSVFormat reflectedResult = (CSVFormat) privateWithCommentMarker.invoke(original, Character.valueOf(commentChar));
        assertEquals(Character.valueOf(commentChar), reflectedResult.getCommentMarker());

        // Invoke with null Character argument (should unset comment marker)
        CSVFormat resultNull = (CSVFormat) privateWithCommentMarker.invoke(original, (Object) null);
        assertNull(resultNull.getCommentMarker());
    }
}