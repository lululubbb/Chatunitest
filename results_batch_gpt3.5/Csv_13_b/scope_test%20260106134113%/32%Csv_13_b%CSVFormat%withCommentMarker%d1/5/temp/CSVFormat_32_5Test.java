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

class CSVFormat_32_5Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_PrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char marker = '#';

        CSVFormat updated = original.withCommentMarker(marker);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(marker), updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_CharacterNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat updated = (CSVFormat) method.invoke(original, new Object[] { null });

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_CharacterValue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character marker = '*';

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat updated = (CSVFormat) method.invoke(original, marker);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(marker, updated.getCommentMarker());
    }
}