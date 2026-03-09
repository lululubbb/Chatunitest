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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_32_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_PrimitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentMarker(commentChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
        // original format should remain unchanged (immutable)
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_CharacterNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { null });

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        // original format unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_CharacterValue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = Character.valueOf(';');

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        CSVFormat result = (CSVFormat) method.invoke(format, commentChar);

        assertNotNull(result);
        assertEquals(commentChar, result.getCommentMarker());
        // original format unchanged
        assertNull(format.getCommentMarker());
    }
}