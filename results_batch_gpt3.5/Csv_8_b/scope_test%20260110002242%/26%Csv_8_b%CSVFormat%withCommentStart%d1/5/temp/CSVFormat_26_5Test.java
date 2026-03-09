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

class CSVFormat_26_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentStart(commentChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentStart());

        // Original format remains unchanged
        assertNull(format.getCommentStart());

        // Test with a different comment char
        char anotherCommentChar = ';';
        CSVFormat anotherFormat = format.withCommentStart(anotherCommentChar);
        assertEquals(Character.valueOf(anotherCommentChar), anotherFormat.getCommentStart());

        // Test with comment char as line break char (allowed, no validation in focal method)
        char lineBreakChar = '\n';
        CSVFormat lineBreakFormat = format.withCommentStart(lineBreakChar);
        assertEquals(Character.valueOf(lineBreakChar), lineBreakFormat.getCommentStart());
    }
}