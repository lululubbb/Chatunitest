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
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
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
    void testPrint_nullObject_newRecordFalse() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "testValue";
        boolean newRecord = false;

        printMethod.invoke(csvFormat, null, value, 0, value.length(), out, newRecord);

        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "hello";
        boolean newRecord = true;

        printMethod.invoke(csvFormat, null, value, 0, value.length(), out, newRecord);

        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        Appendable outMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "quotedValue";
        boolean newRecord = false;
        Object object = 123;

        // Spy on CSVFormat to verify private method call
        CSVFormat spyFormat = spy(format);

        // Use reflection to invoke private method printAndQuote
        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        // Do nothing when printAndQuote is called to isolate test
        doAnswer(invocation -> {
            Appendable appendable = invocation.getArgument(4);
            appendable.append("quoted");
            return null;
        }).when(spyFormat).printAndQuote(any(), any(), anyInt(), anyInt(), any(), anyBoolean());

        printMethod.invoke(spyFormat, object, value, 0, value.length(), outMock, newRecord);

        verify(spyFormat).printAndQuote(object, value, 0, value.length(), outMock, newRecord);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Appendable outMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "escapeValue";
        boolean newRecord = true;
        Object object = new Object();

        // Spy on CSVFormat to verify private method call
        CSVFormat spyFormat = spy(format);

        // Use reflection to invoke private method printAndEscape
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        doAnswer(invocation -> {
            Appendable appendable = invocation.getArgument(3);
            appendable.append("escaped");
            return null;
        }).when(spyFormat).printAndEscape(any(), anyInt(), anyInt(), any());

        printMethod.invoke(spyFormat, object, value, 0, value.length(), outMock, newRecord);

        verify(spyFormat).printAndEscape(value, 0, value.length(), outMock);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Appendable outMock = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "simpleValue";
        boolean newRecord = false;
        Object object = new Object();

        printMethod.invoke(format, object, value, 1, 6, outMock, newRecord);

        verify(outMock).append(format.getDelimiter());
        verify(outMock).append(value, 1, 7);
        verifyNoMoreInteractions(outMock);
    }
}