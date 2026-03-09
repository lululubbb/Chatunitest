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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        // newRecord = true, object = null
        printMethod.invoke(csvFormat, null, value, 0, value.length(), appendable, true);

        verify(appendable, never()).append(csvFormat.getDelimiter());
        verify(appendable).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        // newRecord = false, object = null
        printMethod.invoke(csvFormat, null, value, 0, value.length(), appendable, false);

        verify(appendable).append(csvFormat.getDelimiter());
        verify(appendable).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat formatWithQuote = csvFormat.withQuote('\"');
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "quotedValue";
        Object object = 123; // Number to cover printAndQuote path
        // newRecord = false
        printMethod.invoke(formatWithQuote, object, value, 0, value.length(), appendable, false);

        verify(appendable).append(formatWithQuote.getDelimiter());
        // printAndQuote is private, so verify append calls indirectly by verifying append called at least once after delimiter
        verify(appendable, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat formatWithEscape = csvFormat.withEscape('\\').withQuote(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "escapedValue";
        Object object = "stringObject";
        // newRecord = true
        printMethod.invoke(formatWithEscape, object, value, 0, value.length(), appendable, true);

        verify(appendable, never()).append(formatWithEscape.getDelimiter());
        // printAndEscape is private, verify append called at least once
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat formatNoQuoteNoEscape = csvFormat.withQuote(null).withEscape(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "plainValue";
        Object object = "stringObject";
        // newRecord = false
        // Adjusted length parameter to avoid IndexOutOfBoundsException: value.length() is 10, so offset=1, len=5 means offset+len=6, which is valid
        printMethod.invoke(formatNoQuoteNoEscape, object, value, 1, 5, appendable, false);

        verify(appendable).append(formatNoQuoteNoEscape.getDelimiter());
        verify(appendable).append(value, 1, 6);
    }

}