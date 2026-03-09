package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_55_4Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_null() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_empty() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeaderComments();
        assertNotNull(result);
        assertNotSame(original, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_variousObjects() {
        CSVFormat original = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "comment1", 123, null, new StringBuilder("comment4") };
        CSVFormat result = original.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(original, result);
        String[] headerComments = result.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(comments.length, headerComments.length);
        assertEquals("comment1", headerComments[0]);
        assertEquals("123", headerComments[1]);
        assertNull(headerComments[2]);
        assertEquals("comment4", headerComments[3]);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_immutability() {
        CSVFormat original = CSVFormat.DEFAULT.withHeaderComments("a", "b");
        CSVFormat result = original.withHeaderComments("c");
        assertNotSame(original, result);
        assertArrayEquals(new String[] { "a", "b" }, original.getHeaderComments());
        assertArrayEquals(new String[] { "c" }, result.getHeaderComments());
    }
}