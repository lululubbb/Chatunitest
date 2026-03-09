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

public class CSVFormat_24_3Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenNull() throws Exception {
        // Create a new CSVFormat instance with commentMarker explicitly set to null using withCommentMarker
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);

        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenSet() throws Exception {
        // Create instance with commentMarker set via withCommentMarker method
        CSVFormat formatWithComment = CSVFormat.DEFAULT.withCommentMarker('#');

        // Using reflection to verify commentMarker is set
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarkerValue = (Character) commentMarkerField.get(formatWithComment);

        assertNotNull(commentMarkerValue);
        assertEquals('#', commentMarkerValue.charValue());
        assertTrue(formatWithComment.isCommentMarkerSet());
    }
}