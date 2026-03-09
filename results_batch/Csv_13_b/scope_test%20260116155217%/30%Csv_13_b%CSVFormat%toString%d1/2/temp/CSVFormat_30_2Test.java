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

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_30_2Test {

    @Test
    @Timeout(8000)
    public void testToString_Defaults() {
        CSVFormat format = CSVFormat.DEFAULT;
        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<,>"));
        assertTrue(toString.contains("Escape=<\\>") || !format.isEscapeCharacterSet());
        assertTrue(toString.contains("QuoteChar=<\">") || !format.isQuoteCharacterSet());
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertTrue(toString.contains("RecordSeparator=<\r\n>"));
        assertTrue(toString.contains("EmptyLines:ignored") == format.getIgnoreEmptyLines());
        assertTrue(toString.contains("SurroundingSpaces:ignored") == format.getIgnoreSurroundingSpaces());
        assertTrue(toString.contains("IgnoreHeaderCase:ignored") == format.getIgnoreHeaderCase());
        assertFalse(toString.contains("HeaderComments:"));
        assertFalse(toString.contains("Header:"));
    }

    @Test
    @Timeout(8000)
    public void testToString_AllFieldsSet() throws Exception {
        String[] header = new String[] {"col1", "col2"};
        String[] headerComments = new String[] {"comment1", "comment2"};

        // Start from DEFAULT and set fields via reflection since constructor is private
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "delimiter", ';');
        setField(format, "quoteCharacter", '\"');
        setField(format, "quoteMode", QuoteMode.ALL);
        setField(format, "commentMarker", '#');
        setField(format, "escapeCharacter", '\\');
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "recordSeparator", "\n");
        setField(format, "nullString", "NULL");
        setField(format, "headerComments", headerComments);
        setField(format, "header", header);
        setField(format, "skipHeaderRecord", true);
        setField(format, "allowMissingColumnNames", false);
        setField(format, "ignoreHeaderCase", false);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<;>"));
        assertTrue(toString.contains("Escape=<\\>"));
        assertTrue(toString.contains("QuoteChar=<\">"));
        assertTrue(toString.contains("CommentStart=<#>"));
        assertTrue(toString.contains("NullString=<NULL>"));
        assertTrue(toString.contains("RecordSeparator=<\n>"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertTrue(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:true"));
        assertTrue(toString.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(toString.contains("Header:[col1, col2]"));
    }

    @Test
    @Timeout(8000)
    public void testToString_NoOptionalFields() throws Exception {
        // Start from DEFAULT and set fields via reflection since constructor is private
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "delimiter", '|');
        setField(format, "quoteCharacter", null);
        setField(format, "quoteMode", null);
        setField(format, "commentMarker", null);
        setField(format, "escapeCharacter", null);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "recordSeparator", null);
        setField(format, "nullString", null);
        setField(format, "headerComments", null);
        setField(format, "header", null);
        setField(format, "skipHeaderRecord", false);
        setField(format, "allowMissingColumnNames", false);
        setField(format, "ignoreHeaderCase", false);

        String toString = format.toString();

        assertTrue(toString.contains("Delimiter=<|>"));
        assertFalse(toString.contains("Escape=<"));
        assertFalse(toString.contains("QuoteChar=<"));
        assertFalse(toString.contains("CommentStart=<"));
        assertFalse(toString.contains("NullString=<"));
        assertFalse(toString.contains("RecordSeparator=<"));
        assertFalse(toString.contains("EmptyLines:ignored"));
        assertFalse(toString.contains("SurroundingSpaces:ignored"));
        assertFalse(toString.contains("IgnoreHeaderCase:ignored"));
        assertTrue(toString.contains("SkipHeaderRecord:false"));
        assertFalse(toString.contains("HeaderComments:"));
        assertFalse(toString.contains("Header:"));
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}