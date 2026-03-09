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

class CSVFormat_13_5Test {

    @Test
    @Timeout(8000)
    void testGetHeaderComments_Null() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has headerComments == null
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_NonNull() throws Exception {
        String[] comments = new String[]{"comment1", "comment2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) comments);

        // Use reflection to access the private headerComments field since
        // withHeaderComments(Object...) returns a new CSVFormat instance with private final field set
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object fieldValue = headerCommentsField.get(format);
        assertNotNull(fieldValue);
        assertTrue(fieldValue instanceof String[]);
        String[] headerCommentsValue = (String[]) fieldValue;

        assertEquals(2, headerCommentsValue.length);
        assertEquals("comment1", headerCommentsValue[0]);
        assertEquals("comment2", headerCommentsValue[1]);

        String[] headerComments = format.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(2, headerComments.length);
        assertEquals("comment1", headerComments[0]);
        assertEquals("comment2", headerComments[1]);

        // Verify that returned array is a clone (modifying it does not affect original)
        headerComments[0] = "changed";
        String[] headerComments2 = format.getHeaderComments();
        assertEquals("comment1", headerComments2[0]);
    }
}