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
    private Appendable out;

    @BeforeEach
    void setup() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    private void invokePrint(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse_appendsDelimiterAndValue() throws Throwable {
        // Arrange
        boolean newRecord = false;
        CharSequence value = "value123";
        int offset = 0;
        int len = value.length();

        // Act
        try {
            invokePrint(null, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // Assert
        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue_appendsValueOnly() throws Throwable {
        boolean newRecord = true;
        CharSequence value = "value123";
        int offset = 0;
        int len = value.length();

        try {
            invokePrint(null, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_callsPrintAndQuote() throws Throwable {
        // Create CSVFormat with quote character set
        csvFormat = CSVFormat.DEFAULT.withQuote('"');
        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;
        Object object = 12345; // Number to test original object usage

        // Spy on csvFormat to verify private printAndQuote call
        CSVFormat spyFormat = spy(csvFormat);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        // We cannot directly verify private method call easily, so we mock out Appendable and verify interactions
        Appendable appendableMock = mock(Appendable.class);

        // Use reflection invoke on spyFormat to call print
        try {
            printMethod.invoke(spyFormat, object, value, offset, len, appendableMock, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // Verify that printAndQuote was called by spying the spyFormat's printAndQuote method
        // Since printAndQuote is private, we verify by partial mocking and verify private method call via Mockito (not supported),
        // so instead verify appendableMock interactions to confirm printAndQuote behavior

        // The printAndQuote method appends quotes and value, so verify append called at least once
        verify(appendableMock, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_callsPrintAndEscape() throws Throwable {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        CharSequence value = "escapeValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;
        Object object = "test";

        Appendable appendableMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        try {
            printMethod.invoke(csvFormat, object, value, offset, len, appendableMock, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // printAndEscape appends characters, verify append called at least once
        verify(appendableMock, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape_appendsValueRange() throws Throwable {
        csvFormat = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        CharSequence value = "0123456789";
        int offset = 2;
        int len = 5; // will append chars from index 2 to 6 exclusive (2+5=7)
        boolean newRecord = false;
        Object object = "test";

        Appendable appendableMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        try {
            printMethod.invoke(csvFormat, object, value, offset, len, appendableMock, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(appendableMock).append(csvFormat.getDelimiter());
        verify(appendableMock).append(value, offset, offset + len);
        verifyNoMoreInteractions(appendableMock);
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_appendsDelimiterFirst() throws Throwable {
        CharSequence value = "abc";
        int offset = 0;
        int len = 3;
        boolean newRecord = false;

        Appendable appendableMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        try {
            printMethod.invoke(csvFormat, null, value, offset, len, appendableMock, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(appendableMock).append(csvFormat.getDelimiter());
        verify(appendableMock).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_noDelimiterAppended() throws Throwable {
        CharSequence value = "abc";
        int offset = 0;
        int len = 3;
        boolean newRecord = true;

        Appendable appendableMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        try {
            printMethod.invoke(csvFormat, null, value, offset, len, appendableMock, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        verify(appendableMock, never()).append(csvFormat.getDelimiter());
        verify(appendableMock).append(value);
    }

}