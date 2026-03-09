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
    void testPrint_nullObject_newRecordTrue() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        try {
            printMethod.invoke(csvFormat, new Object[] {null, value, offset, len, out, newRecord});
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }

        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        char delimiter = csvFormat.getDelimiter();

        try {
            printMethod.invoke(csvFormat, new Object[] {null, value, offset, len, out, newRecord});
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }

        verify(out).append(delimiter);
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        StringBuilder spyOut = spy(new StringBuilder());

        try {
            printMethod.invoke(format, new Object[] {"object", value, offset, len, spyOut, newRecord});
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }

        String output = spyOut.toString();
        assertTrue(output.startsWith("\""));
        assertTrue(output.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        StringBuilder sb = new StringBuilder();

        try {
            printMethod.invoke(format, new Object[] {"object", value, offset, len, sb, newRecord});
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }

        String output = sb.toString();
        assertTrue(output.contains("value"));
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value123";
        int offset = 1;
        int len = 4;
        boolean newRecord = false;

        StringBuilder sb = new StringBuilder();

        try {
            printMethod.invoke(format, new Object[] {"obj", value, offset, len, sb, newRecord});
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }

        char delimiter = format.getDelimiter();
        assertTrue(sb.toString().startsWith(String.valueOf(delimiter)));
        assertEquals("alue", sb.toString().substring(1));
    }
}