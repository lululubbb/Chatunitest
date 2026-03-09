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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormatEqualsTest {

    @Test
    @Timeout(8000)
    public void testEquals_sameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullObject() {
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
    public void testEquals_allFieldsEqual() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "delimiter", (char) (format1.getDelimiter() + 1));
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        QuoteMode differentQuoteMode = format1.getQuoteMode() == QuoteMode.ALL_NON_NULL ? QuoteMode.MINIMAL : QuoteMode.ALL_NON_NULL;
        setField(format2, "quoteMode", differentQuoteMode);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "quoteCharacter", null);
        if (format1.getQuoteCharacter() != null) {
            assertFalse(format1.equals(format2));
            assertFalse(format2.equals(format1));
        } else {
            assertTrue(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_quoteCharacter_differentValue() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character qc = format1.getQuoteCharacter();
        if (qc == null) {
            setField(format2, "quoteCharacter", 'Q');
            assertFalse(format1.equals(format2));
        } else {
            char different = qc == 'Q' ? 'Z' : 'Q';
            setField(format2, "quoteCharacter", different);
            assertFalse(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "commentMarker", null);
        if (format1.getCommentMarker() != null) {
            assertFalse(format1.equals(format2));
            assertFalse(format2.equals(format1));
        } else {
            assertTrue(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_commentMarker_differentValue() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character cm = format1.getCommentMarker();
        if (cm == null) {
            setField(format2, "commentMarker", '#');
            assertFalse(format1.equals(format2));
        } else {
            char different = cm == '#' ? '!' : '#';
            setField(format2, "commentMarker", different);
            assertFalse(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "escapeCharacter", null);
        if (format1.getEscapeCharacter() != null) {
            assertFalse(format1.equals(format2));
            assertFalse(format2.equals(format1));
        } else {
            assertTrue(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_escapeCharacter_differentValue() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        Character ec = format1.getEscapeCharacter();
        if (ec == null) {
            setField(format2, "escapeCharacter", '\\');
            assertFalse(format1.equals(format2));
        } else {
            char different = ec == '\\' ? '/' : '\\';
            setField(format2, "escapeCharacter", different);
            assertFalse(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "nullString", null);
        if (format1.getNullString() != null) {
            assertFalse(format1.equals(format2));
            assertFalse(format2.equals(format1));
        } else {
            assertTrue(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_nullString_differentValue() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        String ns = format1.getNullString();
        if (ns == null) {
            setField(format2, "nullString", "null");
            assertFalse(format1.equals(format2));
        } else {
            setField(format2, "nullString", ns + "diff");
            assertFalse(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_differentLength() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "header", new String[] {"a", "b"});
        setField(format1, "header", new String[] {"a"});
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_header_differentContent() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "header", new String[] {"a", "b"});
        setField(format1, "header", new String[] {"a", "c"});
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreSurroundingSpaces() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "ignoreSurroundingSpaces", !format1.getIgnoreSurroundingSpaces());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_ignoreEmptyLines() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "ignoreEmptyLines", !format1.getIgnoreEmptyLines());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_skipHeaderRecord() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "skipHeaderRecord", !format1.getSkipHeaderRecord());
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_nullAndNonNull() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        setField(format2, "recordSeparator", null);
        if (format1.getRecordSeparator() != null) {
            assertFalse(format1.equals(format2));
            assertFalse(format2.equals(format1));
        } else {
            assertTrue(format1.equals(format2));
        }
    }

    @Test
    @Timeout(8000)
    public void testEquals_recordSeparator_differentValue() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = copyCSVFormat(format1);
        String rs = format1.getRecordSeparator();
        if (rs == null) {
            setField(format2, "recordSeparator", "\n");
            assertFalse(format1.equals(format2));
        } else {
            setField(format2, "recordSeparator", rs + "diff");
            assertFalse(format1.equals(format2));
        }
    }

    private CSVFormat copyCSVFormat(CSVFormat original) throws Exception {
        // Use reflection to call private constructor with all fields copied
        Class<?> clazz = CSVFormat.class;
        Field delimiterField = clazz.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(original);

        Field quoteCharField = clazz.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(original);

        Field quoteModeField = clazz.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);

        Field commentMarkerField = clazz.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(original);

        Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(original);

        Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);

        Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);

        Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(original);

        Field nullStringField = clazz.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(original);

        Field headerCommentsField = clazz.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);

        Field headerField = clazz.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(original);

        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);

        Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);

        Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);

        Field trimField = clazz.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(original);

        Field trailingDelimiterField = clazz.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(original);

        Field autoFlushField = clazz.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);
        boolean autoFlush = autoFlushField.getBoolean(original);

        // Constructor signature:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)
        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}