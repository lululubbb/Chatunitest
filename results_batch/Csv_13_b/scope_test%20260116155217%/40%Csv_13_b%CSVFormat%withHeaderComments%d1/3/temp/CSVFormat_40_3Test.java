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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_40_3Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NewInstanceWithHeaderComments() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call withHeaderComments with varargs
        CSVFormat updated = original.withHeaderComments("Comment1", "Comment2");

        // Verify new instance is created and original is unchanged
        assertNotSame(original, updated);

        // Verify headerComments field is set correctly via getter
        String[] headerComments = updated.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(2, headerComments.length);
        assertEquals("Comment1", headerComments[0]);
        assertEquals("Comment2", headerComments[1]);

        // Verify other fields remain equal (example: delimiter)
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertEquals(original.getNullString(), updated.getNullString());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NoArgumentsCreatesEmptyHeaderComments() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat updated = original.withHeaderComments();

        assertNotSame(original, updated);

        String[] headerComments = updated.getHeaderComments();
        assertNotNull(headerComments);
        assertEquals(0, headerComments.length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_ReflectionInvoke() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getDeclaredMethod("withHeaderComments", Object[].class);
        method.setAccessible(true);

        // Invoke withHeaderComments with varargs via reflection:
        // The single argument to invoke must be Object[] containing the varargs array
        CSVFormat updated = (CSVFormat) method.invoke(original, (Object) new Object[]{"ReflectComment"});

        assertNotNull(updated);
        assertNotSame(original, updated);

        String[] comments = updated.getHeaderComments();
        assertNotNull(comments);
        assertEquals(1, comments.length);
        assertEquals("ReflectComment", comments[0]);
    }
}