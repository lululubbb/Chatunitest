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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_57_4Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments((Object[]) null);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNull(result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_EmptyHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeaderComments(new Object[0]);
        assertNotNull(result);
        assertNotSame(format, result);
        assertNotNull(result.getHeaderComments());
        assertEquals(0, result.getHeaderComments().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleHeaderComment() {
        CSVFormat format = CSVFormat.DEFAULT;
        String comment = "header comment";
        CSVFormat result = format.withHeaderComments((Object) comment);
        assertNotNull(result);
        assertNotSame(format, result);
        assertArrayEquals(new String[] { comment }, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_MultipleHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT;
        Object[] comments = new Object[] { "comment1", 123, null, "comment4" };
        CSVFormat result = format.withHeaderComments(comments);
        assertNotNull(result);
        assertNotSame(format, result);
        assertArrayEquals(new String[] { "comment1", "123", null, "comment4" }, result.getHeaderComments());
    }
}