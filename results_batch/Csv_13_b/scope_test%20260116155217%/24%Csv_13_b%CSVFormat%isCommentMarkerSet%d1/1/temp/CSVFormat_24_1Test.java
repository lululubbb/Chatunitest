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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_24_1Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenCommentMarkerIsNull() throws Exception {
        // Create a new CSVFormat instance with commentMarker set (non-null)
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');

        // Using reflection to access private final field commentMarker
        Field field = CSVFormat.class.getDeclaredField("commentMarker");
        field.setAccessible(true);

        // Remove final modifier from the commentMarker field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set commentMarker to null using reflection
        field.set(format, null);

        // Invoke isCommentMarkerSet and assert false
        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenCommentMarkerIsSet() throws Exception {
        // Create CSVFormat instance with commentMarker set
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');

        // Using reflection to access private final field commentMarker
        Field field = CSVFormat.class.getDeclaredField("commentMarker");
        field.setAccessible(true);

        // Assert that commentMarker is not null
        assertNotNull(field.get(format));
        // Invoke isCommentMarkerSet and assert true
        assertTrue(format.isCommentMarkerSet());
    }
}