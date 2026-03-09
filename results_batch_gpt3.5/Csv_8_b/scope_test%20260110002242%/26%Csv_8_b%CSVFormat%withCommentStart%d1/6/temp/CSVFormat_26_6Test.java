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

class CSVFormat_26_6Test {

    @Test
    @Timeout(8000)
    void testWithCommentStart_withChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat updated = original.withCommentStart(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentStart());
        // Original remains unchanged
        assertNull(original.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testWithCommentStart_withChar_nullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '\0';

        CSVFormat updated = original.withCommentStart(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getCommentStart());
    }
}