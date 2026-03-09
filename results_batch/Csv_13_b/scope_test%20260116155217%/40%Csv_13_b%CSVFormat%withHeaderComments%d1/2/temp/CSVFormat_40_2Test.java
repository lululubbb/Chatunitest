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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_40_2Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments();
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Object[] comments = new Object[] {"comment1", "comment2", 123, null};
        CSVFormat result = baseFormat.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        String[] headerComments = result.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(comments.length, headerComments.length);
        assertEquals("comment1", headerComments[0]);
        assertEquals("comment2", headerComments[1]);
        assertEquals("123", headerComments[2]);
        assertNull(headerComments[3]);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Immutability() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withHeaderComments("initial");
        CSVFormat modified = baseFormat.withHeaderComments("newComment");
        assertNotSame(baseFormat, modified);
        assertArrayEquals(new String[]{"initial"}, baseFormat.getHeaderComments());
        assertArrayEquals(new String[]{"newComment"}, modified.getHeaderComments());
    }
}