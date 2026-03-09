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

public class CSVFormat_13_5Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        // Create a new CSVFormat instance based on DEFAULT
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private final headerComments field to null
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Remove final modifier via reflection (hack)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);

        headerCommentsField.set(format, null);

        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        String[] originalComments = new String[] {"comment1", "comment2"};
        // Use withHeaderComments to create a new CSVFormat instance with headerComments set
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) originalComments);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null array when headerComments is set");
        assertArrayEquals(originalComments, result, "Returned array should match original headerComments");
        assertNotSame(originalComments, result, "Returned array should be a clone, not the same instance");

        // Modify returned array to ensure original is not affected
        result[0] = "modified";
        String[] afterModification = format.getHeaderComments();
        assertEquals("comment1", afterModification[0], "Original headerComments array should not be affected by changes to the clone");
    }
}