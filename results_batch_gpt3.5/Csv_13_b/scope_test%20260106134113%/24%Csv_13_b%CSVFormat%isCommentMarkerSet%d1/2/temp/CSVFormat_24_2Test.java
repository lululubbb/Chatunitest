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

class CSVFormat_24_2Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private final field commentMarker to null
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier using reflection (works in Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        commentMarkerField.set(format, null);

        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private final field commentMarker to '#'
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier using reflection (works in Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        commentMarkerField.set(format, Character.valueOf('#'));

        assertTrue(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_withWithCommentMarkerMethod() {
        CSVFormat formatWithComment = CSVFormat.DEFAULT.withCommentMarker('#');
        assertTrue(formatWithComment.isCommentMarkerSet());

        CSVFormat formatWithNullComment = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        assertFalse(formatWithNullComment.isCommentMarkerSet());
    }
}