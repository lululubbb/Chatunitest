package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_57_3Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments((Object[]) null);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeaderComments(new Object[0]);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNotNull(newFormat.getHeaderComments());
        assertEquals(0, newFormat.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleHeaderComment() {
        CSVFormat format = CSVFormat.DEFAULT;
        String comment = "Header comment";
        CSVFormat newFormat = format.withHeaderComments(comment);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertArrayEquals(new String[] { comment }, newFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "Comment1", "Comment2", 123, null };
        CSVFormat newFormat = format.withHeaderComments(comments);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        String[] headerComments = newFormat.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(comments.length, headerComments.length);
        for (int i = 0; i < comments.length; i++) {
            assertEquals(comments[i] == null ? "null" : comments[i].toString(), headerComments[i]);
        }
    }
}