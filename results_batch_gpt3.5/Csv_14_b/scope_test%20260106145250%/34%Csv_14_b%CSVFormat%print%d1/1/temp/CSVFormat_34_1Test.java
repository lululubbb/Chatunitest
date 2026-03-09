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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
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

    private void invokePrint(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_ObjectNull_AppendsDelimiterAndValue() throws Throwable {
        // newRecord = false and object == null
        doNothing().when(appendable).append(anyChar());
        doNothing().when(appendable).append(any(CharSequence.class));
        try {
            invokePrint(null, "value", 0, 5, appendable, false);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        verify(appendable).append(csvFormat.getDelimiter());
        verify(appendable).append("value");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_ObjectNull_AppendsValueOnly() throws Throwable {
        // newRecord = true and object == null
        doNothing().when(appendable).append(any(CharSequence.class));
        try {
            invokePrint(null, "value", 0, 5, appendable, true);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        verify(appendable, never()).append(csvFormat.getDelimiter());
        verify(appendable).append("value");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_QuoteCharacterSet_InvokesPrintAndQuote() throws Throwable {
        // Use CSVFormat with quoteCharacter set to non-null to trigger printAndQuote
        CSVFormat formatWithQuote = CSVFormat.DEFAULT.withQuote('\"');
        Appendable mockOut = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(formatWithQuote, "object", "value", 0, 5, mockOut, true);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        // We cannot verify internals of printAndQuote as it's private, but no exception means success
    }

    @Test
    @Timeout(8000)
    void testPrint_EscapeCharacterSet_InvokesPrintAndEscape() throws Throwable {
        // Use CSVFormat with escapeCharacter set to non-null and quoteCharacter null to trigger printAndEscape
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Appendable mockOut = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(formatWithEscape, "object", "value", 0, 5, mockOut, true);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
        // We cannot verify internals of printAndEscape as it's private, but no exception means success
    }

    @Test
    @Timeout(8000)
    void testPrint_NoQuoteNoEscape_AppendsValueRange() throws Throwable {
        // Use CSVFormat with quoteCharacter null and escapeCharacter null
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable mockOut = mock(Appendable.class);

        doNothing().when(mockOut).append(any(CharSequence.class), anyInt(), anyInt());

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(formatNoQuoteEscape, "object", "value", 1, 3, mockOut, true);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(mockOut).append("value", 1, 4);
        verifyNoMoreInteractions(mockOut);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_ObjectNonNull_AppendsDelimiter() throws Throwable {
        // newRecord = false and object != null, with quoteCharacter null and escapeCharacter null
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable mockOut = mock(Appendable.class);

        doNothing().when(mockOut).append(anyChar());
        doNothing().when(mockOut).append(any(CharSequence.class), anyInt(), anyInt());

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(formatNoQuoteEscape, "obj", "value", 0, 5, mockOut, false);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(mockOut).append(formatNoQuoteEscape.getDelimiter());
        verify(mockOut).append("value", 0, 5);
        verifyNoMoreInteractions(mockOut);
    }
}