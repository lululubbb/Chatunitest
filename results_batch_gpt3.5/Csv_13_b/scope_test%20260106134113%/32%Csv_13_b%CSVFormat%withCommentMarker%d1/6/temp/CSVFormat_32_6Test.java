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

class CSVFormat_32_6Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarkerChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char marker = '#';
        CSVFormat modified = original.withCommentMarker(marker);
        assertNotNull(modified);
        assertEquals(Character.valueOf(marker), modified.getCommentMarker());
        // Original should remain unchanged
        assertNull(original.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerCharacter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character marker = '@';

        Method withCommentMarkerCharacter = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        withCommentMarkerCharacter.setAccessible(true);

        CSVFormat modified = (CSVFormat) withCommentMarkerCharacter.invoke(original, (Object) marker);
        assertNotNull(modified);
        assertEquals(marker, modified.getCommentMarker());
        // Original should remain unchanged
        assertNull(original.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerNullCharacter() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method withCommentMarkerCharacter = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        withCommentMarkerCharacter.setAccessible(true);

        CSVFormat modified = (CSVFormat) withCommentMarkerCharacter.invoke(original, (Object) null);
        assertNotNull(modified);
        assertNull(modified.getCommentMarker());
        // Original should remain unchanged
        assertNull(original.getCommentMarker());
    }

}