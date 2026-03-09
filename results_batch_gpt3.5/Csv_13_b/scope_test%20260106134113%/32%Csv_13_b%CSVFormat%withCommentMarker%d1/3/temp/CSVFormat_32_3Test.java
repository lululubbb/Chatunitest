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

class CSVFormat_32_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarkerChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
        assertNotSame(baseFormat, newFormat);

        // Also verify original format unchanged
        assertNull(baseFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerCharacter() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character commentChar = ';';

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(baseFormat, commentChar);

        assertNotNull(newFormat);
        assertEquals(commentChar, newFormat.getCommentMarker());
        assertNotSame(baseFormat, newFormat);

        // Original format unchanged
        assertNull(baseFormat.getCommentMarker());

        // Test with null Character disables comment marker
        CSVFormat noComment = (CSVFormat) method.invoke(baseFormat, new Object[] { null });
        assertNotNull(noComment);
        assertNull(noComment.getCommentMarker());
    }
}