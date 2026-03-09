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

public class CSVFormat_13_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // headerComments is null in DEFAULT, so getHeaderComments() should return null
        String[] headerComments = format.getHeaderComments();
        assertNull(headerComments);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");

        // Use reflection to access private headerComments field
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] original = (String[]) headerCommentsField.get(format);

        String[] returned = format.getHeaderComments();

        assertNotNull(returned);
        assertArrayEquals(original, returned);
        assertNotSame(original, returned);

        // Modify returned array should not affect original field
        returned[0] = "modified";
        String[] afterModification = (String[]) headerCommentsField.get(format);
        assertEquals("comment1", afterModification[0]);
    }
}