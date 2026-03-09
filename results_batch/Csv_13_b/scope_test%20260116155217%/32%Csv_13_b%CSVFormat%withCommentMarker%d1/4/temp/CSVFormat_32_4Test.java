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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

class CSVFormatWithCommentMarkerTest {

    private CSVFormat defaultFormat;

    @BeforeEach
    void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerPrimitiveChar() {
        char commentChar = '#';
        CSVFormat newFormat = defaultFormat.withCommentMarker(commentChar);
        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());
        // Original format should remain unchanged
        assertNull(defaultFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerCharacterNull() throws Exception {
        Method withCommentMarkerCharObjMethod = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        withCommentMarkerCharObjMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withCommentMarkerCharObjMethod.invoke(defaultFormat, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerCharacterNonNull() throws Exception {
        Method withCommentMarkerCharObjMethod = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        withCommentMarkerCharObjMethod.setAccessible(true);

        Character commentChar = '@';
        CSVFormat result = (CSVFormat) withCommentMarkerCharObjMethod.invoke(defaultFormat, commentChar);
        assertNotNull(result);
        assertEquals(commentChar, result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarkerPrimitiveCharReflection() throws Exception {
        Method withCommentMarkerPrimitiveMethod = CSVFormat.class.getDeclaredMethod("withCommentMarker", char.class);
        withCommentMarkerPrimitiveMethod.setAccessible(true);

        char commentChar = '%';
        // Pass primitive char, not Character object
        CSVFormat result = (CSVFormat) withCommentMarkerPrimitiveMethod.invoke(defaultFormat, commentChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
    }
}