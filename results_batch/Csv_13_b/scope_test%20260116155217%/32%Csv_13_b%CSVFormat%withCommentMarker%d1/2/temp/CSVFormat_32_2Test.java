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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class CSVFormat_32_2Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat format = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = format.withCommentMarker(commentChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
        // Original format should remain unchanged (immutability)
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_Character_null() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access public withCommentMarker(Character) method
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        // Passing null should return a CSVFormat with null commentMarker
        CSVFormat result = (CSVFormat) method.invoke(format, (Object) null);

        assertNotNull(result);
        assertNull(result.getCommentMarker());
        // Original format should remain unchanged
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_Character_nonNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentChar = '!';

        // Use reflection to access public withCommentMarker(Character) method
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);

        CSVFormat result = (CSVFormat) method.invoke(format, commentChar);

        assertNotNull(result);
        assertEquals(commentChar, result.getCommentMarker());
        // Original format should remain unchanged
        assertNull(format.getCommentMarker());
    }
}