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

class CSVFormat_32_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarkerPrimitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = format.withCommentMarker(commentChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerNullCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        CSVFormat newFormat = (CSVFormat) method.invoke(format, new Object[] { (Character) null });

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerCharacterObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = '*';

        // Use reflection to invoke public withCommentMarker(Character)
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        CSVFormat newFormat = (CSVFormat) method.invoke(format, commentChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(commentChar, newFormat.getCommentMarker());
    }
}