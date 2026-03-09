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
import java.lang.reflect.Modifier;

class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has headerComments null by default
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        String[] comments = new String[] {"comment1", "comment2"};

        // Create a new CSVFormat instance with headerComments set via reflection
        CSVFormat formatWithComments = CSVFormat.DEFAULT;

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Remove final modifier from the field (Java 12+ requires this workaround)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);

        headerCommentsField.set(formatWithComments, comments.clone());

        String[] returnedComments = formatWithComments.getHeaderComments();

        assertNotNull(returnedComments);
        assertArrayEquals(comments, returnedComments);
        // Ensure returned array is a clone, not the same reference
        assertNotSame(comments, returnedComments);

        // Modify returned array should not affect original
        returnedComments[0] = "changed";
        String[] returnedComments2 = formatWithComments.getHeaderComments();
        assertEquals("comment1", returnedComments2[0]);
    }
}