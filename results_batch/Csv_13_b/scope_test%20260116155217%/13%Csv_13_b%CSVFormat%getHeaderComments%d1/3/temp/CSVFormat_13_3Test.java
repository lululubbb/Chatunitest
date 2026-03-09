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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_13_3Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        // Use reflection to create CSVFormat instance with headerComments set
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object[] headerComments = new Object[]{"comment1", "comment2"};
        String[] header = new String[]{"header1", "header2"};

        CSVFormat format = constructor.newInstance(
                ',', '"', null, null, null,
                false, true, "\r\n", null,
                headerComments, header,
                false, false, false);

        String[] comments1 = format.getHeaderComments();
        assertNotNull(comments1);
        assertEquals(2, comments1.length);
        assertArrayEquals(new String[]{"comment1", "comment2"}, comments1);

        // Ensure returned array is a clone, not the original array
        String[] comments2 = format.getHeaderComments();
        assertNotSame(comments1, comments2);

        // Modifying returned array does not affect internal state
        comments1[0] = "changed";
        String[] comments3 = format.getHeaderComments();
        assertEquals("comment1", comments3[0]);
    }
}