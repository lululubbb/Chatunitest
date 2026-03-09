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

class CSVFormat_19_2Test {

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsNull() throws Exception {
        // Use withCommentStart(Character) with null explicitly
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart((Character) null);

        assertFalse(format.isCommentingEnabled());
    }

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        assertTrue(format.isCommentingEnabled());
    }

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsCharacterObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart(Character.valueOf(';'));
        assertTrue(format.isCommentingEnabled());
    }
}