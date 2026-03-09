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

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validCharacter() {
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
    void testWithCommentMarker_nullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat newFormat = format.withCommentMarker((Character) null);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacter_throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        char[] lineBreaks = {'\r', '\n'};
        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharacterWrapper_throws() {
        CSVFormat format = CSVFormat.DEFAULT;

        Character[] lineBreaks = {'\r', '\n'};
        for (Character lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                format.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", thrown.getMessage());
        }
    }
}