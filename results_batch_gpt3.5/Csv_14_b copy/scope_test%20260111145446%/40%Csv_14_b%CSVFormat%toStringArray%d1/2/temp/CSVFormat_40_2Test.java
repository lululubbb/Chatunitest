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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_40_2Test {

    @Test
    @Timeout(8000)
    public void testToStringArray() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] values = {"value1", "value2", null, "value3"};
        String[] expected = {"value1", "value2", null, "value3"};

        try {
            CSVFormat csvFormat = createCSVFormat(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                    skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

            String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) values);

            assertArrayEquals(expected, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, Object[] headerComments, String[] header, boolean skipHeaderRecord,
            boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        try {
            Method constructor = CSVFormat.class.getDeclaredMethod("CSVFormat", char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return (CSVFormat) constructor
                .invoke(null, delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                        recordSeparator, nullString, headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                        ignoreHeaderCase, trim, trailingDelimiter);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
            Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, Object[] headerComments, String[] header, boolean skipHeaderRecord,
            boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        try {
            Method constructor = CSVFormat.class.getDeclaredMethod("CSVFormat", char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return (CSVFormat) constructor
                .invoke(null, delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                        recordSeparator, nullString, headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                        ignoreHeaderCase, trim, trailingDelimiter);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}