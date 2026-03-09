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
import org.junit.jupiter.api.BeforeEach;

class CSVFormatWithHeaderCommentsTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Null() {
        CSVFormat format = baseFormat.withHeaderComments((Object[]) null);
        assertNotNull(format);
        assertNotSame(baseFormat, format);
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Empty() {
        CSVFormat format = baseFormat.withHeaderComments();
        assertNotNull(format);
        assertNotSame(baseFormat, format);
        assertNotNull(format.getHeaderComments());
        assertEquals(0, format.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleString() {
        CSVFormat format = baseFormat.withHeaderComments("comment1");
        assertNotNull(format);
        assertNotSame(baseFormat, format);
        String[] comments = format.getHeaderComments();
        assertNotNull(comments);
        assertEquals(1, comments.length);
        assertEquals("comment1", comments[0]);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleStrings() {
        CSVFormat format = baseFormat.withHeaderComments("comment1", "comment2", "comment3");
        assertNotNull(format);
        assertNotSame(baseFormat, format);
        String[] comments = format.getHeaderComments();
        assertNotNull(comments);
        assertEquals(3, comments.length);
        assertArrayEquals(new String[]{"comment1", "comment2", "comment3"}, comments);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MixedObjects() {
        CSVFormat format = baseFormat.withHeaderComments("comment", 123, null, true);
        assertNotNull(format);
        assertNotSame(baseFormat, format);
        String[] comments = format.getHeaderComments();
        assertNotNull(comments);
        assertEquals(4, comments.length);
        assertEquals("comment", comments[0]);
        assertEquals("123", comments[1]);
        assertNull(comments[2]);
        assertEquals("true", comments[3]);
    }
}