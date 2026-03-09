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

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_primitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentMarker(commentChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { null });

        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_sameCommentMarkerReturnsSameInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Using reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        Character currentCommentMarker = format.getCommentMarker();

        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { currentCommentMarker });

        // If comment marker is same, the method may return same instance
        if (currentCommentMarker == null) {
            assertSame(format, result);
        } else {
            assertNotNull(result);
        }
    }
}