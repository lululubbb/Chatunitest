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
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_ObjectNull_NewRecordTrue_AppendsValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "value";
        boolean newRecord = true;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, null, value, 0, value.length(), out, newRecord);

        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_ObjectNull_NewRecordFalse_AppendsDelimiterAndValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "value";
        boolean newRecord = false;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, null, value, 0, value.length(), out, newRecord);

        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_QuoteCharacterSet_InvokesPrintAndQuote() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "quoted";
        boolean newRecord = false;
        Object object = 123; // Number to test number branch

        // Use a subclass to spy private printAndQuote method call since printAndQuote is private
        CSVFormat spyFormat = spy(csvFormat);
        doReturn(true).when(spyFormat).isQuoteCharacterSet();
        doReturn(false).when(spyFormat).isEscapeCharacterSet();

        // Mock printAndQuote to verify it is called
        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);
        doNothing().when(spyFormat).printAndQuote(object, value, 0, value.length(), out, newRecord);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyFormat, object, value, 0, value.length(), out, newRecord);

        verify(spyFormat).printAndQuote(object, value, 0, value.length(), out, newRecord);
        verify(out).append(spyFormat.getDelimiter());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_EscapeCharacterSet_InvokesPrintAndEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "escaped";
        boolean newRecord = true;
        Object object = "object";

        CSVFormat spyFormat = spy(csvFormat);
        doReturn(false).when(spyFormat).isQuoteCharacterSet();
        doReturn(true).when(spyFormat).isEscapeCharacterSet();

        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
        doNothing().when(spyFormat).printAndEscape(value, 0, value.length(), out);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyFormat, object, value, 0, value.length(), out, newRecord);

        verify(spyFormat, never()).printAndQuote(any(), any(), anyInt(), anyInt(), any(), anyBoolean());
        verify(spyFormat).printAndEscape(value, 0, value.length(), out);
        verify(out, never()).append(spyFormat.getDelimiter());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_NoQuoteNoEscape_AppendsValueRange() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "abcdefghij";
        boolean newRecord = false;
        Object object = "obj";

        CSVFormat spyFormat = spy(csvFormat);
        doReturn(false).when(spyFormat).isQuoteCharacterSet();
        doReturn(false).when(spyFormat).isEscapeCharacterSet();

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyFormat, object, value, 2, 5, out, newRecord);

        verify(out).append(spyFormat.getDelimiter());
        verify(out).append(value, 2, 7);
        verifyNoMoreInteractions(out);
    }
}