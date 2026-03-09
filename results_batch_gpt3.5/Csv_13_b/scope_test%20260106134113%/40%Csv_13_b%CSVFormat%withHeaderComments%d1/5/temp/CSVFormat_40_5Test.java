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

class CSVFormat_40_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullHeaderComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyHeaderComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        String[] comments = result.getHeaderComments();
        assertNotNull(comments);
        assertEquals(0, comments.length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleString() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments((Object) "comment1");
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        String[] comments = result.getHeaderComments();
        assertNotNull(comments);
        assertArrayEquals(new String[]{"comment1"}, comments);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleObjects() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments("comment1", 123, null, true);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        String[] comments = result.getHeaderComments();
        assertNotNull(comments);
        assertArrayEquals(new String[]{"comment1", "123", null, "true"}, comments);
    }
}