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

class CSVFormat_13_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() throws Exception {
        // Create a new CSVFormat instance with headerComments set to null using withHeaderComments()
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object[]) null);

        // Using reflection to set headerComments to null explicitly for testing
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Remove final modifier from the field (Java 12+ requires special handling; here is a common approach)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerCommentsField, headerCommentsField.getModifiers() & ~Modifier.FINAL);

        headerCommentsField.set(format, null);

        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsCloneNotSameArray() {
        CSVFormat formatWithComments = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");
        String[] comments = formatWithComments.getHeaderComments();
        assertNotNull(comments);
        assertArrayEquals(new String[]{"comment1", "comment2"}, comments);

        // Modify returned array should not affect internal state
        comments[0] = "modified";
        String[] commentsAfterModify = formatWithComments.getHeaderComments();
        assertEquals("comment1", commentsAfterModify[0]);
    }
}