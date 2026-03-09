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

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char marker = '#';

        CSVFormat updated = original.withCommentMarker(marker);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(marker), updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_nullChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char marker = '\0';

        CSVFormat updated = original.withCommentMarker(marker);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_sameMarker() {
        Character existingMarker = Character.valueOf('#');
        CSVFormat original = CSVFormat.DEFAULT.withCommentMarker(existingMarker);
        char marker = '#';

        CSVFormat updated = original.withCommentMarker(marker);

        assertSame(original, updated);
    }
}