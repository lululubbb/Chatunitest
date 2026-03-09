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
import org.junit.jupiter.api.DisplayName;

class CSVFormat_40_3Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test withHeaderComments with various inputs")
    void testWithHeaderComments() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with no header comments (null)
        CSVFormat formatEmpty = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(formatEmpty);
        assertNotSame(baseFormat, formatEmpty);
        assertNull(formatEmpty.getHeaderComments());

        // Test with single header comment
        CSVFormat formatSingle = baseFormat.withHeaderComments("Comment1");
        assertNotNull(formatSingle);
        assertNotSame(baseFormat, formatSingle);
        String[] commentsSingle = formatSingle.getHeaderComments();
        assertNotNull(commentsSingle);
        assertEquals(1, commentsSingle.length);
        assertEquals("Comment1", commentsSingle[0]);

        // Test with multiple header comments
        CSVFormat formatMultiple = baseFormat.withHeaderComments("Comment1", "Comment2", "Comment3");
        assertNotNull(formatMultiple);
        assertNotSame(baseFormat, formatMultiple);
        String[] commentsMultiple = formatMultiple.getHeaderComments();
        assertNotNull(commentsMultiple);
        assertEquals(3, commentsMultiple.length);
        assertArrayEquals(new String[]{"Comment1", "Comment2", "Comment3"}, commentsMultiple);

        // Test with null in header comments
        CSVFormat formatWithNull = baseFormat.withHeaderComments("Comment1", null, "Comment3");
        assertNotNull(formatWithNull);
        assertNotSame(baseFormat, formatWithNull);
        String[] commentsWithNull = formatWithNull.getHeaderComments();
        assertNotNull(commentsWithNull);
        assertEquals(3, commentsWithNull.length);
        assertEquals("Comment1", commentsWithNull[0]);
        assertNull(commentsWithNull[1]);
        assertEquals("Comment3", commentsWithNull[2]);
    }
}