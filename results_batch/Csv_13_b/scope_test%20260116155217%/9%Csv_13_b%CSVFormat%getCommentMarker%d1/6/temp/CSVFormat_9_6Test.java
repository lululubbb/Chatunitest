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

import java.lang.reflect.Field;

public class CSVFormat_9_6Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatWithComment;

    @BeforeEach
    public void setUp() throws Exception {
        // Use the public DEFAULT instance for default commentMarker (null)
        csvFormatDefault = CSVFormat.DEFAULT;

        // Create a CSVFormat instance with commentMarker set via withCommentMarker(char)
        csvFormatWithComment = CSVFormat.DEFAULT.withCommentMarker(Character.valueOf('#'));
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_DefaultIsNull() {
        assertNull(csvFormatDefault.getCommentMarker(), "Default commentMarker should be null");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithCommentMarker() {
        Character commentMarker = csvFormatWithComment.getCommentMarker();
        assertNotNull(commentMarker, "CommentMarker should not be null");
        assertEquals(Character.valueOf('#'), commentMarker, "CommentMarker should be '#'");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_ReflectionAccess() throws Exception {
        // Access private final field commentMarker via reflection for csvFormatWithComment
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarkerValue = (Character) commentMarkerField.get(csvFormatWithComment);
        assertEquals(Character.valueOf('#'), commentMarkerValue, "Reflection: commentMarker should be '#'");
    }
}