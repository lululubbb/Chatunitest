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

public class CSVFormat_13_2Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_Null() throws Exception {
        // Create a new CSVFormat instance with headerComments = null via withHeaderComments
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);
        // Use reflection to forcibly set private final field headerComments to null to simulate the test case
        setFinalField(format, "headerComments", null);

        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_NotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");
        String[] comments = new String[]{"comment1", "comment2"};
        // Use reflection to forcibly set private final field headerComments to comments array
        setFinalField(format, "headerComments", comments);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null when headerComments is set");
        assertArrayEquals(comments, result, "Returned array should be equal to original");

        // Verify that returned array is a clone, not the same reference
        assertNotSame(comments, result, "Returned array should be a clone, not the same reference");

        // Modify returned array and verify original is not changed
        result[0] = "modified";
        String[] original = (String[]) getField(format, "headerComments");
        assertEquals("comment1", original[0], "Original array should not be affected by modification of returned array");
    }

    private Object getField(Object target, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}