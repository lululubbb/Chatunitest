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

class CSVFormat_28_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char comment = '#';

        CSVFormat result = original.withCommentMarker(comment);

        assertNotNull(result);
        assertEquals(Character.valueOf(comment), result.getCommentMarker());

        // original should remain unchanged (immutability)
        assertNull(original.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_withNullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char comment = '\0';

        CSVFormat result = original.withCommentMarker(comment);

        assertNotNull(result);
        // When comment char is '\0', getCommentMarker() returns null (no comment marker)
        assertNull(result.getCommentMarker());
        assertNull(original.getCommentMarker());
    }
}