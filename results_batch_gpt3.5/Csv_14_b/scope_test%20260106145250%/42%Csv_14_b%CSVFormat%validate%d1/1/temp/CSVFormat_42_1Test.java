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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private void invokeValidate(CSVFormat format) throws Throwable {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        try {
            validate.invoke(format);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
            Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines, String recordSeparator, String nullString, Object[] headerComments,
            String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames,
            boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter) throws Exception {
        // Use reflection to call the private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterLineBreak() throws Throwable {
        CSVFormat format = createCSVFormat('\n', null, QuoteMode.NONE, null, null, false, false, "\n", null, null, null,
                false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("delimiter cannot be a line break")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsQuoteChar() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf(','), QuoteMode.NONE, null, null, false, false, "\n", null,
                null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("quoteChar character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsEscapeChar() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.NONE, null, Character.valueOf(','), false,
                false, "\n", null, null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("escape character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterEqualsCommentMarker() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.NONE, Character.valueOf(','), null, false,
                false, "\n", null, null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsCommentMarker() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf('#'), QuoteMode.NONE, Character.valueOf('#'), null, false,
                false, "\n", null, null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start character and the quoteChar cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsCommentMarker() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.NONE, Character.valueOf('#'),
                Character.valueOf('#'), false, false, "\n", null, null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start and the escape character cannot be the same")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharWithQuoteModeNone() throws Throwable {
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.NONE, null, null, false, false, "\n", null,
                null, null, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("No quotes mode set but no escape character is set")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicates() throws Throwable {
        String[] headerWithDup = new String[] { "a", "b", "a" };
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, null, Character.valueOf('\\'),
                false, false, "\n", null, null, headerWithDup, false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("header contains a duplicate entry")) {
            fail("Unexpected exception message: " + thrown.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidateSuccess() throws Throwable {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = createCSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, Character.valueOf('#'),
                Character.valueOf('\\'), false, false, "\n", null, null, header, false, false, false, false, false);
        invokeValidate(format);
    }
}