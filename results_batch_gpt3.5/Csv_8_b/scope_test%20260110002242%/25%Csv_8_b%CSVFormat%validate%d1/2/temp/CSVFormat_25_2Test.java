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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_25_2Test {

    private CSVFormat csvFormat;
    private Method validateMethod;
    private Constructor<CSVFormat> csvFormatConstructor;

    @BeforeEach
    public void setUp() throws Exception {
        // Access private validate method via reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);

        // Access private constructor via reflection
        csvFormatConstructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        csvFormatConstructor.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        validateMethod.invoke(format);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, CSVFormat.Quote quotePolicy,
                                      Character commentStart, Character escape,
                                      boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString,
                                      String[] header, boolean skipHeaderRecord) throws Exception {
        return csvFormatConstructor.newInstance(delimiter, quoteChar, quotePolicy,
                commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, header, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_default() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        csvFormat = createCSVFormat(
                ',',
                Character.valueOf(','),
                CSVFormat.Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsDelimiter_throws() throws Exception {
        csvFormat = createCSVFormat(
                ';',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                null,
                Character.valueOf(';'),
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentStartEqualsDelimiter_throws() throws Exception {
        csvFormat = createCSVFormat(
                '#',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                Character.valueOf('#'),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentStart_throws() throws Exception {
        csvFormat = createCSVFormat(
                ',',
                Character.valueOf('!'),
                CSVFormat.Quote.MINIMAL,
                Character.valueOf('!'),
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeEqualsCommentStart_throws() throws Exception {
        csvFormat = createCSVFormat(
                ',',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                Character.valueOf('\\'),
                Character.valueOf('\\'),
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeNullAndQuoteNone_throws() throws Exception {
        csvFormat = createCSVFormat(
                ',',
                null,
                CSVFormat.Quote.NONE,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("No quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerDuplicates_throws() throws Exception {
        String[] header = new String[] { "a", "b", "a" };
        csvFormat = createCSVFormat(
                ',',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                header,
                false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(csvFormat));
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("The header contains duplicate names"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerUnique_noException() throws Exception {
        String[] header = new String[] { "a", "b", "c" };
        csvFormat = createCSVFormat(
                ',',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                header,
                false);
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_allValid_noException() throws Exception {
        csvFormat = createCSVFormat(
                ';',
                Character.valueOf('"'),
                CSVFormat.Quote.MINIMAL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                false,
                true,
                "\r\n",
                null,
                new String[] { "header1", "header2" },
                false);
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }
}