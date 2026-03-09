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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_44_6Test {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Use DEFAULT instance as base
        csvFormat = CSVFormat.DEFAULT;

        // Access private validate method via reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n');
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        // message contains "delimiter cannot be a line break"
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf(','),
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                ';',
                Character.valueOf('"'),
                null,
                null,
                Character.valueOf(';'),
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateCommentMarkerEqualsDelimiter() throws Exception {
        CSVFormat format = new CSVFormat(
                '#',
                Character.valueOf('"'),
                null,
                Character.valueOf('#'),
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsCommentMarker() throws Exception {
        Character c = Character.valueOf('"');
        CSVFormat format = new CSVFormat(
                ',',
                c,
                null,
                c,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsCommentMarker() throws Exception {
        Character c = Character.valueOf('!');
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                null,
                c,
                c,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharNullAndQuoteModeNone() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                null,
                QuoteMode.NONE,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicates() throws Exception {
        String[] headerWithDuplicates = new String[] { "A", "B", "A" };
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                headerWithDuplicates,
                false,
                false,
                false,
                false,
                false,
                false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateValidConfiguration() throws Exception {
        String[] uniqueHeader = new String[] { "A", "B", "C" };
        CSVFormat format = new CSVFormat(
                ',',
                Character.valueOf('"'),
                QuoteMode.MINIMAL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                false,
                false,
                "\r\n",
                null,
                null,
                uniqueHeader,
                false,
                false,
                false,
                false,
                false,
                false);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}