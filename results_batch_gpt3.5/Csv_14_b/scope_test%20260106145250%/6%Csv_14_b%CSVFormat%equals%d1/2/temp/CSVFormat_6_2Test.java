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
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_6_2Test {

    @Test
    @Timeout(8000)
    public void testEquals_reflexive() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_null() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("some string"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "delimiter", (char)(format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        QuoteMode differentQuoteMode = format1.getQuoteMode() == QuoteMode.ALL ? QuoteMode.MINIMAL : QuoteMode.ALL;
        setField(format2, "quoteMode", differentQuoteMode);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);

        // quoteCharacter null in format1, non-null in format2
        setField(format1, "quoteCharacter", null);
        setField(format2, "quoteCharacter", Character.valueOf('Q'));
        assertFalse(format1.equals(format2));

        // quoteCharacter non-null in format1, null in format2
        setField(format1, "quoteCharacter", Character.valueOf('Q'));
        setField(format2, "quoteCharacter", null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format1, "quoteCharacter", Character.valueOf('Q'));
        setField(format2, "quoteCharacter", Character.valueOf('W'));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);

        setField(format1, "commentMarker", null);
        setField(format2, "commentMarker", Character.valueOf('C'));
        assertFalse(format1.equals(format2));

        setField(format1, "commentMarker", Character.valueOf('C'));
        setField(format2, "commentMarker", null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format1, "commentMarker", Character.valueOf('C'));
        setField(format2, "commentMarker", Character.valueOf('D'));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);

        setField(format1, "escapeCharacter", null);
        setField(format2, "escapeCharacter", Character.valueOf('E'));
        assertFalse(format1.equals(format2));

        setField(format1, "escapeCharacter", Character.valueOf('E'));
        setField(format2, "escapeCharacter", null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format1, "escapeCharacter", Character.valueOf('E'));
        setField(format2, "escapeCharacter", Character.valueOf('F'));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);

        setField(format1, "nullString", null);
        setField(format2, "nullString", "null");
        assertFalse(format1.equals(format2));

        setField(format1, "nullString", "null");
        setField(format2, "nullString", null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format1, "nullString", "null1");
        setField(format2, "nullString", "null2");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "c");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format2, "skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);

        setField(format1, "recordSeparator", null);
        setField(format2, "recordSeparator", "\n");
        assertFalse(format1.equals(format2));

        setField(format1, "recordSeparator", "\n");
        setField(format2, "recordSeparator", null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_notEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = cloneCSVFormat(format1);
        setField(format1, "recordSeparator", "\n");
        setField(format2, "recordSeparator", "\r");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_equalObjects() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("a", "b");
        assertTrue(format1.equals(format2));
        assertTrue(format2.equals(format1));
    }

    private static CSVFormat cloneCSVFormat(CSVFormat original) throws Exception {
        // Use reflection to create a new CSVFormat with same fields as original
        // Because constructor is private, use DEFAULT and override fields by reflection
        CSVFormat clone = CSVFormat.DEFAULT;
        for (Field field : CSVFormat.class.getDeclaredFields()) {
            if ((field.getModifiers() & java.lang.reflect.Modifier.FINAL) != 0) {
                continue; // skip final fields
            }
            field.setAccessible(true);
            Object value = field.get(original);
            field.set(clone, value);
        }
        return clone;
    }

    private static void setField(CSVFormat instance, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
}