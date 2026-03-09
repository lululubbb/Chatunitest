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

class CSVFormat_32_1Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentMarker());
        // Original instance should remain unchanged (immutability)
        assertNull(original.getCommentMarker());
        // The returned instance should not be the same as the original
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withCommentMarker('!');

        // Use reflection to invoke public withCommentMarker(Character) method with null
        Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        method.setAccessible(true);
        CSVFormat updated = (CSVFormat) method.invoke(original, new Object[]{null});

        assertNotNull(updated);
        assertNull(updated.getCommentMarker());
        assertEquals(Character.valueOf('!'), original.getCommentMarker());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_Character_setDifferent() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withCommentMarker('!');

        Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        method.setAccessible(true);
        CSVFormat updated = (CSVFormat) method.invoke(original, Character.valueOf('#'));

        assertNotNull(updated);
        assertEquals(Character.valueOf('#'), updated.getCommentMarker());
        assertEquals(Character.valueOf('!'), original.getCommentMarker());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_Character_setSame() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withCommentMarker('!');

        Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        method.setAccessible(true);
        CSVFormat updated = (CSVFormat) method.invoke(original, Character.valueOf('!'));

        // Should return the same instance if comment marker is unchanged
        assertSame(original, updated);
    }
}