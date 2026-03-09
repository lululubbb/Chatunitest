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

public class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_NullHeaderComments() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);
        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_EmptyHeaderComments() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments();
        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null when headerComments is set");
        assertEquals(0, result.length, "Expected returned array to be empty");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments_WithValues() throws Exception {
        String[] comments = new String[] {"comment1", "comment2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) comments);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null when headerComments is set");
        assertArrayEquals(comments, result, "Expected returned array to match the headerComments values");
        assertNotSame(comments, result, "Expected returned array to be a clone, not the same instance");

        // Modify returned array and verify original is not affected
        result[0] = "modified";

        // Use reflection to get the private field headerComments
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] original = (String[]) headerCommentsField.get(format);
        assertEquals("comment1", original[0], "Original headerComments should not be affected by changes to returned array");
    }
}