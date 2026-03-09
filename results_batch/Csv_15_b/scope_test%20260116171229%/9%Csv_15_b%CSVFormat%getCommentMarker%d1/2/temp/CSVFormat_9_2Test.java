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

public class CSVFormat_9_2Test {

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_whenCommentMarkerIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        // Remove final modifier from the field (works for JDK 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        commentMarkerField.set(format, null);

        Character result = format.getCommentMarker();
        assertNull(result, "Expected commentMarker to be null");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_whenCommentMarkerIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentMarkerField, commentMarkerField.getModifiers() & ~Modifier.FINAL);

        commentMarkerField.set(format, '#');

        Character result = format.getCommentMarker();
        assertNotNull(result, "Expected commentMarker not to be null");
        assertEquals(Character.valueOf('#'), result, "Expected commentMarker to be '#'");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_withNewInstanceAndCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('!');

        Character result = format.getCommentMarker();
        assertNotNull(result);
        assertEquals(Character.valueOf('!'), result);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_withNewInstanceAndNullCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);

        Character result = format.getCommentMarker();
        assertNull(result);
    }
}