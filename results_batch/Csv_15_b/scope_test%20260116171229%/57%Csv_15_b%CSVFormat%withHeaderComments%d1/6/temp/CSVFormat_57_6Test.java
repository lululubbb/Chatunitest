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

import java.lang.reflect.Field;

class CSVFormatWithHeaderCommentsTest {

    private String[] getHeaderComments(CSVFormat format) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("headerComments");
        field.setAccessible(true);
        return (String[]) field.get(format);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_nullComments() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(getHeaderComments(result));
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_emptyComments() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments();
        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(getHeaderComments(result));
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_singleComment() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments("comment1");
        assertNotNull(result);
        assertNotSame(format, result);

        String[] comments = getHeaderComments(result);
        assertNotNull(comments);
        assertEquals(1, comments.length);
        assertEquals("comment1", comments[0]);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_multipleComments() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments("comment1", "comment2", "comment3");
        assertNotNull(result);
        assertNotSame(format, result);

        String[] comments = getHeaderComments(result);
        assertNotNull(comments);
        assertEquals(3, comments.length);
        assertArrayEquals(new String[] {"comment1", "comment2", "comment3"}, comments);
    }

}