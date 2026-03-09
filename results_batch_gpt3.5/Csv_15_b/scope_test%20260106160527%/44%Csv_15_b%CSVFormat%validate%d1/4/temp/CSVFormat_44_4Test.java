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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Create a new CSVFormat instance by copying DEFAULT and modifying fields via reflection
        csvFormat = createMutableCSVFormat();

        // Access private validate method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private CSVFormat createMutableCSVFormat() throws Exception {
        // Use reflection to create a new CSVFormat instance by copying DEFAULT fields
        CSVFormat base = CSVFormat.DEFAULT;

        // Get the constructor with all arguments
        // Constructor signature (char, Character, QuoteMode, Character, Character, boolean, boolean, String, String, Object[], String[], boolean, boolean, boolean, boolean, boolean, boolean)
        Class<?> cls = CSVFormat.class;
        java.lang.reflect.Constructor<?> constructor = cls.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        // Extract all field values from DEFAULT instance
        char delimiter = base.getDelimiter();
        Character quoteChar = base.getQuoteCharacter();
        QuoteMode quoteMode = base.getQuoteMode();
        Character commentStart = base.getCommentMarker();
        Character escape = base.getEscapeCharacter();
        boolean ignoreSurroundingSpaces = (boolean) getFieldValue(base, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = base.getIgnoreEmptyLines();
        String recordSeparator = base.getRecordSeparator();
        String nullString = base.getNullString();
        Object[] headerComments = (Object[]) getFieldValue(base, "headerComments");
        String[] header = base.getHeader();
        boolean skipHeaderRecord = (boolean) getFieldValue(base, "skipHeaderRecord");
        boolean allowMissingColumnNames = base.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = base.getIgnoreHeaderCase();
        boolean trim = base.getTrim();
        boolean trailingDelimiter = base.getTrailingDelimiter();
        boolean autoFlush = (boolean) getFieldValue(base, "autoFlush");

        return (CSVFormat) constructor.newInstance(delimiter, quoteChar, quoteMode,
                commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }

    private Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(csvFormat, value);
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() throws Exception {
        setField("delimiter", '\n');

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsQuoteCharacter() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf(','));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsEscapeCharacter() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", Character.valueOf(','));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("commentMarker", Character.valueOf(','));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharacterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf('#'));
        setField("commentMarker", Character.valueOf('#'));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharacterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", Character.valueOf('#'));
        setField("commentMarker", Character.valueOf('#'));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharacterWithQuoteModeNone() throws Exception {
        setField("escapeCharacter", null);
        setField("quoteMode", QuoteMode.NONE);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicates() throws Exception {
        setField("header", new String[] { "A", "B", "A" });

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            invokeValidate();
        });
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderNoDuplicates() throws Exception {
        setField("header", new String[] { "A", "B", "C" });
        // Should pass without exception
        invokeValidate();
    }

    @Test
    @Timeout(8000)
    void testValidateAllValid() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf('"'));
        setField("escapeCharacter", Character.valueOf('\\'));
        setField("commentMarker", Character.valueOf('#'));
        setField("quoteMode", QuoteMode.MINIMAL);
        setField("header", new String[] { "A", "B", "C" });

        // Should pass without exception
        invokeValidate();
    }

    private void invokeValidate() throws Exception {
        try {
            validateMethod.invoke(csvFormat);
        } catch (InvocationTargetException e) {
            // unwrap the cause to throw the actual exception thrown by validate()
            throw (Exception) e.getCause();
        }
    }
}