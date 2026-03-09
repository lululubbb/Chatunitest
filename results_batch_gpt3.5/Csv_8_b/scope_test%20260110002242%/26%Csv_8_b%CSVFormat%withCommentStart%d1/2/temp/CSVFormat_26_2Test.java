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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_26_2Test {

    @Test
    @Timeout(8000)
    void testWithCommentStart_char() {
        char commentChar = '#';
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withCommentStart(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStart_char_sameAsOriginal() {
        Character commentChar = CSVFormat.DEFAULT.getCommentStart();
        CSVFormat original = CSVFormat.DEFAULT;
        // When commentChar is null, withCommentStart(null) returns same instance
        CSVFormat updated = original.withCommentStart(commentChar);
        assertSame(original, updated);
    }
}