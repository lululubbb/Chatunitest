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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_28_1Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_null() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character commentChar = null;

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_reflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '!';

        // Using reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(format, commentChar);  // pass char directly, autoboxing applies

        assertNotNull(result);
        assertNotSame(format, result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
    }
}