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

public class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        // Create a new CSVFormat instance with headerComments explicitly set to null
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);

        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        String[] originalComments = new String[]{"comment1", "comment2"};

        // Create a new CSVFormat instance with headerComments set via withHeaderComments
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) originalComments);

        String[] result = format.getHeaderComments();

        assertNotNull(result, "Expected non-null array when headerComments field is set");
        assertArrayEquals(originalComments, result, "Expected returned array to match original comments");
        assertNotSame(originalComments, result, "Expected returned array to be a clone, not the same instance");

        // Modifying returned array should not affect original
        result[0] = "modified";

        // Use reflection to get the private headerComments field
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] afterModification = (String[]) headerCommentsField.get(format);

        assertEquals("comment1", afterModification[0], "Original headerComments should not be affected by modifications to returned array");
    }
}