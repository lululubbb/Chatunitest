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

class CSVFormat_33_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentMarker = '#';
        CSVFormat newFormat = format.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
        // Original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = null;
        CSVFormat newFormat = format.withCommentMarker(commentMarker);
        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharThrows() {
        CSVFormat format = CSVFormat.DEFAULT;
        char[] lineBreaks = { '\n', '\r' };
        for (char lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(Character.valueOf(lb));
            });
            assertEquals("The comment start marker character cannot be a line break", ex.getMessage());
        }
    }
}