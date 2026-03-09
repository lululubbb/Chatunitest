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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_24_6Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // commentMarker is private final, but withCommentMarker(null) creates new instance with null commentMarker
        CSVFormat newFormat = format.withCommentMarker((Character) null);
        assertFalse(newFormat.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_whenCommentMarkerIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        assertTrue(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_reflectivelySetCommentMarkerNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');

        CSVFormat newFormat = createCSVFormatWithCommentMarker(format, null);

        assertFalse(newFormat.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSet_reflectivelySetCommentMarkerChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = createCSVFormatWithCommentMarker(format, '#');
        assertTrue(newFormat.isCommentMarkerSet());
    }

    // Helper method to create a new CSVFormat instance with the same fields as original but with specified commentMarker
    private CSVFormat createCSVFormatWithCommentMarker(CSVFormat original, Character commentMarker) throws Exception {
        // Use withCommentMarker to get a new instance with desired commentMarker (or null)
        CSVFormat baseFormat = (commentMarker == null) ? original.withCommentMarker((Character) null) : original.withCommentMarker(commentMarker);

        // Now forcibly set the private final commentMarker field to the desired value via reflection
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        commentMarkerField.set(baseFormat, commentMarker);

        return baseFormat;
    }
}