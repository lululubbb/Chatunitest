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

class CSVFormat_40_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments((Object[]) null);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[0]);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNotNull(newFormat.getHeaderComments());
        assertEquals(0, newFormat.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleComment() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[] { "comment1" });
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[]{"comment1"}, newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[] {"comment1", "comment2", "comment3"});
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[]{"comment1", "comment2", "comment3"}, newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MixedTypeComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[] {"comment1", 123, null, true});
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        String[] expected = new String[]{"comment1", "123", "null", "true"};
        assertArrayEquals(expected, newFormat.getHeaderComments());
    }
}