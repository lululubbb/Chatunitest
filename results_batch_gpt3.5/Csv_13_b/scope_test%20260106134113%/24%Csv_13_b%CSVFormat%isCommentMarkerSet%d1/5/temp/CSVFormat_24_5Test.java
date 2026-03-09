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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_24_5Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsNull() throws Exception {
        // Use default CSVFormat which has commentMarker == null
        CSVFormat format = CSVFormat.DEFAULT;

        // commentMarker is private final, use reflection to set it to null
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier from the field (works in Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        // Since commentMarker is final and a Character, set it to null via reflection
        commentMarkerField.set(format, null);

        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsSet() {
        // Create a CSVFormat instance with a comment marker
        CSVFormat formatWithComment = CSVFormat.DEFAULT.withCommentMarker('#');
        assertTrue(formatWithComment.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_withNullCharacterCommentMarker() {
        // Create a CSVFormat instance with commentMarker explicitly set to null Character object
        CSVFormat formatWithNullChar = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        assertFalse(formatWithNullChar.isCommentMarkerSet());
    }
}