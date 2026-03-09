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

class CSVFormat_40_6Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeaderComments());
        // Other properties remain unchanged
        assertArrayEquals(baseFormat.getHeader(), result.getHeader());
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleComment() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String comment = "This is a header comment";
        CSVFormat result = baseFormat.withHeaderComments((Object) comment);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertArrayEquals(new String[] { comment }, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "Comment1", 123, null, "Comment3" };
        CSVFormat result = baseFormat.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertArrayEquals(new String[] { "Comment1", "123", null, "Comment3" }, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_OriginalImmutable() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withHeader("h1", "h2");
        Object[] comments = new Object[] { "c1" };
        CSVFormat result = baseFormat.withHeaderComments(comments);
        // Original headerComments should remain null
        assertNull(baseFormat.getHeaderComments());
        // New instance has headerComments set
        assertArrayEquals(new String[] { "c1" }, result.getHeaderComments());
        // Header remains unchanged
        assertArrayEquals(new String[] { "h1", "h2" }, result.getHeader());
    }
}