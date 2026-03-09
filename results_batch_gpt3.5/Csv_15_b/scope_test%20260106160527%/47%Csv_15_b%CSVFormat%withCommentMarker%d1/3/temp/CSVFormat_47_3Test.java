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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_47_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentMarker());

        // Also test with a different comment char
        char anotherCommentChar = '!';
        CSVFormat updatedAgain = updated.withCommentMarker(anotherCommentChar);
        assertEquals(Character.valueOf(anotherCommentChar), updatedAgain.getCommentMarker());

        // Test with null comment marker using withCommentMarker(Character) directly via reflection
        try {
            Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
            method.setAccessible(true);
            CSVFormat nullCommentFormat = (CSVFormat) method.invoke(original, (Object) null);
            assertNull(nullCommentFormat.getCommentMarker());
        } catch (Exception e) {
            fail("Reflection invocation failed: " + e.getMessage());
        }
    }
}