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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class CSVFormat_13_6Test {

    private CSVFormat csvFormatWithComments;
    private CSVFormat csvFormatWithoutComments;

    @BeforeEach
    public void setUp() throws Exception {
        // Create instance with headerComments set
        csvFormatWithComments = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");

        // Create instance with headerComments null (default)
        csvFormatWithoutComments = CSVFormat.DEFAULT;

        // Using reflection to forcibly set headerComments to null in csvFormatWithoutComments to test null case
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Remove final modifier before setting
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);

        headerCommentsField.set(csvFormatWithoutComments, null);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_whenHeaderCommentsSet() {
        String[] comments = csvFormatWithComments.getHeaderComments();
        assertNotNull(comments, "The returned headerComments array should not be null");
        assertArrayEquals(new String[]{"comment1", "comment2"}, comments, "The returned headerComments should match what was set");

        // Verify that returned array is a clone (modifying returned array should not affect internal state)
        comments[0] = "modified";
        String[] commentsAgain = csvFormatWithComments.getHeaderComments();
        assertEquals("comment1", commentsAgain[0], "Modifying returned array should not affect internal headerComments");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_whenHeaderCommentsNull() throws Exception {
        // Using reflection to forcibly set headerComments to null in csvFormatWithComments instance to test null return
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Remove final modifier before setting
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);

        headerCommentsField.set(csvFormatWithComments, null);

        assertNull(csvFormatWithComments.getHeaderComments(), "getHeaderComments should return null if headerComments field is null");
    }
}