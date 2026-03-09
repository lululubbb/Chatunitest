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

    private void invokePrint(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
            throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_objectNull() throws Throwable {
        // Setup
        doNothing().when(out).append(any(CharSequence.class));
        doNothing().when(out).append(any(char.class));
        // Execute
        invokePrint(null, "value", 0, 5, out, false);
        // Verify
        verify(out).append(csvFormat.getDelimiter());
        verify(out).append("value");
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_objectNull() throws Throwable {
        // Should not append delimiter
        doNothing().when(out).append(any(CharSequence.class));
        doNothing().when(out).append(any(char.class));
        invokePrint(null, "value", 0, 5, out, true);
        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append("value");
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws Throwable {
        csvFormat = csvFormat.withQuote('\"');
        Appendable outSpy = spy(new StringBuilder());
        invokePrint(123, "123", 0, 3, outSpy, false);
        // It should append delimiter first because newRecord is false
        String result = outSpy.toString();
        assertTrue(result.startsWith(String.valueOf(csvFormat.getDelimiter())));
        assertTrue(result.contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws Throwable {
        csvFormat = csvFormat.withEscape('\\').withQuote(null);
        Appendable outSpy = spy(new StringBuilder());
        invokePrint("abc", "abc", 0, 3, outSpy, true);
        String result = outSpy.toString();
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Throwable {
        csvFormat = csvFormat.withQuote(null).withEscape(null);
        Appendable outSpy = spy(new StringBuilder());
        invokePrint("abcdef", "abcdef", 1, 3, outSpy, false);
        String result = outSpy.toString();
        assertTrue(result.startsWith(String.valueOf(csvFormat.getDelimiter())));
        assertEquals("bcd", result.substring(1));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_withQuoteCharacterSet() throws Throwable {
        csvFormat = csvFormat.withQuote('\"');
        Appendable outSpy = spy(new StringBuilder());
        invokePrint(null, "nullValue", 0, 9, outSpy, false);
        String result = outSpy.toString();
        assertTrue(result.startsWith(String.valueOf(csvFormat.getDelimiter())));
        assertTrue(result.contains("nullValue"));
    }
}