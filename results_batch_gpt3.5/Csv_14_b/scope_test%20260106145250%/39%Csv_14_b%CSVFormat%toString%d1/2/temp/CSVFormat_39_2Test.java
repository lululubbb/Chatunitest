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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_39_2Test {

    @Test
    @Timeout(8000)
    public void testToString_DefaultDelimiterOnly() {
        CSVFormat format = CSVFormat.newFormat(';'); // delimiter only, no other settings
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<;>"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_AllFieldsSet() {
        // Create CSVFormat instance with all relevant fields set
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter('|')
                .withEscape('\\')
                .withQuote('"')
                .withCommentMarker('#')
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withIgnoreEmptyLines(true)
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withSkipHeaderRecord(true)
                .withHeader("one", "two")
                .withHeaderComments("comment1", "comment2");

        String str = format.toString();

        assertTrue(str.contains("Delimiter=<|>"));
        assertTrue(str.contains("Escape=<\\>"));
        assertTrue(str.contains("QuoteChar=<\">"));
        assertTrue(str.contains("CommentStart=<#>"));
        assertTrue(str.contains("NullString=<NULL>"));
        assertTrue(str.contains("RecordSeparator=<\n>"));
        assertTrue(str.contains("EmptyLines:ignored"));
        assertTrue(str.contains("SurroundingSpaces:ignored"));
        assertTrue(str.contains("IgnoreHeaderCase:ignored"));
        assertTrue(str.contains("SkipHeaderRecord:true"));
        assertTrue(str.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(str.contains("Header:[one, two]"));
    }

    @Test
    @Timeout(8000)
    public void testToString_NullRecordSeparator() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        String str = format.toString();
        assertFalse(str.contains("RecordSeparator=<"));
    }

    @Test
    @Timeout(8000)
    public void testToString_HeaderAndHeaderCommentsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null).withHeaderComments((Object[]) null);
        String str = format.toString();
        assertFalse(str.contains("HeaderComments:"));
        assertFalse(str.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateMethodsUsingReflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // isEscapeCharacterSet()
        Method isEscapeCharacterSetMethod = CSVFormat.class.getDeclaredMethod("isEscapeCharacterSet");
        isEscapeCharacterSetMethod.setAccessible(true);
        boolean escapeSet = (boolean) isEscapeCharacterSetMethod.invoke(format);
        assertEquals(format.isEscapeCharacterSet(), escapeSet);

        // isQuoteCharacterSet()
        Method isQuoteCharacterSetMethod = CSVFormat.class.getDeclaredMethod("isQuoteCharacterSet");
        isQuoteCharacterSetMethod.setAccessible(true);
        boolean quoteSet = (boolean) isQuoteCharacterSetMethod.invoke(format);
        assertEquals(format.isQuoteCharacterSet(), quoteSet);

        // isCommentMarkerSet()
        Method isCommentMarkerSetMethod = CSVFormat.class.getDeclaredMethod("isCommentMarkerSet");
        isCommentMarkerSetMethod.setAccessible(true);
        boolean commentSet = (boolean) isCommentMarkerSetMethod.invoke(format);
        assertEquals(format.isCommentMarkerSet(), commentSet);

        // isNullStringSet()
        Method isNullStringSetMethod = CSVFormat.class.getDeclaredMethod("isNullStringSet");
        isNullStringSetMethod.setAccessible(true);
        boolean nullStringSet = (boolean) isNullStringSetMethod.invoke(format);
        assertEquals(format.isNullStringSet(), nullStringSet);
    }

    @Test
    @Timeout(8000)
    public void testToString_WithEmptyHeaderAndComments() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader(new String[] {}).withHeaderComments(new Object[] {});
        String str = format.toString();
        // Empty arrays should still print arrays representation
        assertTrue(str.contains("Header:[]"));
        assertTrue(str.contains("HeaderComments:[]"));
    }
}