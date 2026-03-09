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

public class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    void testGetHeaderComments_whenHeaderCommentsIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);
        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_whenHeaderCommentsIsEmptyArray() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments();
        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null when headerComments is empty array");
        assertEquals(0, result.length, "Expected empty array");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_whenHeaderCommentsHasValues() throws Exception {
        String[] comments = new String[]{"Comment1", "Comment2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) comments);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null when headerComments has values");
        assertArrayEquals(comments, result, "Expected returned array to equal the headerComments array");
        assertNotSame(comments, result, "Expected returned array to be a clone, not the same instance");

        // Modify returned array and verify original is not affected
        result[0] = "Modified";

        // Use reflection to get the private headerComments field from format
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] originalAfterModification = (String[]) headerCommentsField.get(format);

        assertEquals("Comment1", originalAfterModification[0], "Original headerComments should not be modified");
    }
}