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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    private void invokePrint(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord) throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_ObjectNull_AppendsDelimiterAndValue() throws Exception {
        // newRecord = false, object = null
        when(out.append(csvFormat.getDelimiter())).thenReturn(out);
        when(out.append(any(CharSequence.class))).thenReturn(out);

        invokePrint(null, "value", 0, 5, out, false);

        verify(out).append(csvFormat.getDelimiter());
        verify(out).append("value");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_ObjectNull_AppendsValueOnly() throws Exception {
        // newRecord = true, object = null
        when(out.append(any(CharSequence.class))).thenReturn(out);

        invokePrint(null, "value", 0, 5, out, true);

        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append("value");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_QuoteCharacterSet() throws Exception {
        // Use CSVFormat with quote character set
        CSVFormat formatWithQuote = CSVFormat.DEFAULT.withQuote('"');
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Spy on formatWithQuote to mock private printAndQuote call
        CSVFormat spyFormat = spy(formatWithQuote);

        // We cannot mock private method easily, so let it run normally
        // The object is not null, so printAndQuote should be called internally

        // We test with a Number object to cover that branch inside printAndQuote (indirectly)
        printMethod.invoke(spyFormat, 123, "123", 0, 3, outMock, false);

        // Verify delimiter appended first
        verify(outMock).append(spyFormat.getDelimiter());
        // We cannot verify private method calls, but no exceptions is success here
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_QuoteCharacterSet() throws Exception {
        CSVFormat formatWithQuote = CSVFormat.DEFAULT.withQuote('"');
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CSVFormat spyFormat = spy(formatWithQuote);

        printMethod.invoke(spyFormat, "quoted", "quoted", 0, 6, outMock, true);

        verify(outMock, never()).append(spyFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_EscapeCharacterSet() throws Exception {
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(formatWithEscape, "escaped", "escaped", 0, 7, outMock, false);

        verify(outMock).append(formatWithEscape.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_EscapeCharacterSet() throws Exception {
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(formatWithEscape, "escaped", "escaped", 0, 7, outMock, true);

        verify(outMock, never()).append(formatWithEscape.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Exception {
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(formatNoQuoteEscape, "plainvalue", "plainvalue", 0, 10, outMock, false);

        verify(outMock).append(formatNoQuoteEscape.getDelimiter());
        verify(outMock).append("plainvalue", 0, 10);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_NoQuoteNoEscape() throws Exception {
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(formatNoQuoteEscape, "plainvalue", "plainvalue", 0, 10, outMock, true);

        verify(outMock, never()).append(formatNoQuoteEscape.getDelimiter());
        verify(outMock).append("plainvalue", 0, 10);
    }

    @Test
    @Timeout(8000)
    public void testPrint_ValueOffsetLength() throws Exception {
        // Confirm substring append with offset and len
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable outMock = mock(Appendable.class);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        String value = "0123456789";
        int offset = 2;
        int len = 5;

        printMethod.invoke(formatNoQuoteEscape, null, value, offset, len, outMock, true);

        verify(outMock).append(value, offset, offset + len);
    }
}