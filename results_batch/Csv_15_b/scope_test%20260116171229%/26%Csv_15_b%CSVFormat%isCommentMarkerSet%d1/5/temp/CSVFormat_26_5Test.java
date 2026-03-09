package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

class CSVFormat_26_5Test {

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenNull() throws Exception {
        // Create a new CSVFormat instance with commentMarker set to a non-null value first
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('!');

        // Access the private final field "commentMarker" in CSVFormat
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        // Set the commentMarker field to null on the instance using reflection
        commentMarkerField.set(format, null);

        boolean result = format.isCommentMarkerSet();
        assertFalse(result, "Expected isCommentMarkerSet() to return false when commentMarker is null");
    }

    @Test
    @Timeout(8000)
    void testIsCommentMarkerSetWhenNotNull() {
        // Create a new CSVFormat instance with commentMarker set to '#'
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');

        boolean result = format.isCommentMarkerSet();
        assertTrue(result, "Expected isCommentMarkerSet() to return true when commentMarker is set");
    }
}