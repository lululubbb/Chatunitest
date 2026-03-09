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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    void testGetHeaderComments_Null() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);
        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_Clone() throws Exception {
        String[] comments = new String[]{"comment1", "comment2"};
        // Cast String[] to Object[] explicitly to match withHeaderComments signature
        Object[] commentsObj = new Object[comments.length];
        System.arraycopy(comments, 0, commentsObj, 0, comments.length);
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments(commentsObj);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null array when headerComments is set");
        assertArrayEquals(comments, result, "Returned array should equal the original array content");
        assertNotSame(comments, result, "Returned array should be a clone, not the same instance");

        // Modify returned array to verify clone behavior
        result[0] = "modified";

        // Use reflection to verify original array inside the format instance
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] afterModification = (String[]) headerCommentsField.get(format);
        assertEquals("comment1", afterModification[0], "Original array should not be affected by changes to clone");
    }
}