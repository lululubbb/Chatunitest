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
import org.junit.jupiter.api.DisplayName;

class CSVFormatWithCommentMarkerTest {

    @Test
    @Timeout(8000)
    @DisplayName("withCommentMarker(char) should set comment marker correctly")
    void testWithCommentMarkerChar() {
        CSVFormat originalFormat = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = originalFormat.withCommentMarker(commentChar);

        assertNotNull(newFormat, "The returned CSVFormat should not be null");
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker(), "Comment marker should be set correctly");
        // Original should remain unchanged
        assertNull(originalFormat.getCommentMarker(), "Original CSVFormat should have null comment marker");
    }

    @Test
    @Timeout(8000)
    @DisplayName("withCommentMarker(Character) should set comment marker correctly")
    void testWithCommentMarkerCharacter() throws Exception {
        CSVFormat originalFormat = CSVFormat.DEFAULT;
        Character commentChar = '*';

        // Use reflection to call public withCommentMarker(Character)
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(originalFormat, commentChar);

        assertNotNull(newFormat, "The returned CSVFormat should not be null");
        assertEquals(commentChar, newFormat.getCommentMarker(), "Comment marker should be set correctly");
        // Original should remain unchanged
        assertNull(originalFormat.getCommentMarker(), "Original CSVFormat should have null comment marker");
    }

    @Test
    @Timeout(8000)
    @DisplayName("withCommentMarker(Character) with null should unset comment marker")
    void testWithCommentMarkerCharacterNull() throws Exception {
        CSVFormat originalFormat = CSVFormat.DEFAULT.withCommentMarker('#');

        // Use reflection to call public withCommentMarker(Character) with null
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(originalFormat, (Object) null);

        assertNotNull(newFormat, "The returned CSVFormat should not be null");
        assertNull(newFormat.getCommentMarker(), "Comment marker should be null after unsetting");
        // Original should remain unchanged
        assertEquals(Character.valueOf('#'), originalFormat.getCommentMarker(), "Original CSVFormat should keep its comment marker");
    }
}