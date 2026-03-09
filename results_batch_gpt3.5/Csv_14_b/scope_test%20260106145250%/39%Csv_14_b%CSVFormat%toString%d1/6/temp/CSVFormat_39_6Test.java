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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

class CSVFormatToStringTest {

    @Test
    @Timeout(8000)
    public void testToString_default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.toString();
        assertTrue(result.contains("Delimiter=<,>"));
        assertTrue(result.contains("QuoteChar=<\""));
        assertTrue(result.contains("SkipHeaderRecord:false"));
    }

    @Test
    @Timeout(8000)
    public void testToString_allFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "delimiter", ';');
        setField(format, "escapeCharacter", Character.valueOf('\\'));
        setField(format, "quoteCharacter", Character.valueOf('\''));
        setField(format, "commentMarker", Character.valueOf('#'));
        setField(format, "nullString", "NULL");
        setField(format, "recordSeparator", "\n");
        setField(format, "ignoreEmptyLines", true);
        setField(format, "ignoreSurroundingSpaces", true);
        setField(format, "ignoreHeaderCase", true);
        setField(format, "skipHeaderRecord", true);
        setField(format, "headerComments", new String[]{"comment1", "comment2"});
        setField(format, "header", new String[]{"header1", "header2"});

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<;>"));
        assertTrue(result.contains("Escape=<\\>"));
        assertTrue(result.contains("QuoteChar=<'"));
        assertTrue(result.contains("CommentStart=<#>"));
        assertTrue(result.contains("NullString=<NULL>"));
        assertTrue(result.contains("RecordSeparator=<\n>"));
        assertTrue(result.contains("EmptyLines:ignored"));
        assertTrue(result.contains("SurroundingSpaces:ignored"));
        assertTrue(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:true"));
        assertTrue(result.contains("HeaderComments:" + Arrays.toString(new String[]{"comment1", "comment2"})));
        assertTrue(result.contains("Header:" + Arrays.toString(new String[]{"header1", "header2"})));
    }

    @Test
    @Timeout(8000)
    public void testToString_noOptionalFieldsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        setField(format, "escapeCharacter", null);
        setField(format, "quoteCharacter", null);
        setField(format, "commentMarker", null);
        setField(format, "nullString", null);
        setField(format, "recordSeparator", null);
        setField(format, "ignoreEmptyLines", false);
        setField(format, "ignoreSurroundingSpaces", false);
        setField(format, "ignoreHeaderCase", false);
        setField(format, "skipHeaderRecord", false);
        setField(format, "headerComments", null);
        setField(format, "header", null);

        String result = format.toString();

        assertTrue(result.contains("Delimiter=<,>"));
        assertFalse(result.contains("Escape=<"));
        assertFalse(result.contains("QuoteChar=<"));
        assertFalse(result.contains("CommentStart=<"));
        assertFalse(result.contains("NullString=<"));
        assertFalse(result.contains("RecordSeparator=<"));
        assertFalse(result.contains("EmptyLines:ignored"));
        assertFalse(result.contains("SurroundingSpaces:ignored"));
        assertFalse(result.contains("IgnoreHeaderCase:ignored"));
        assertTrue(result.contains("SkipHeaderRecord:false"));
        assertFalse(result.contains("HeaderComments:"));
        assertFalse(result.contains("Header:"));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}