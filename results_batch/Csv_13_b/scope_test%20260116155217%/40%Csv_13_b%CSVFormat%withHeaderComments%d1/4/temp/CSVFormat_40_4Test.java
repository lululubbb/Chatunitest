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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_40_4Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Null() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments((Object[]) null);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Empty() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[0]);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNotNull(newFormat.getHeaderComments());
        assertEquals(0, newFormat.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleString() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments((Object) "comment1");
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[]{"comment1"}, newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleObjects() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments("comment1", 123, true);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[]{"comment1", "123", "true"}, newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_OriginalImmutable() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("original");
        CSVFormat newFormat = format.withHeaderComments("new");
        // Original format header comments remain unchanged
        assertArrayEquals(new String[]{"original"}, format.getHeaderComments());
        // New format has new header comments
        assertArrayEquals(new String[]{"new"}, newFormat.getHeaderComments());
    }
}