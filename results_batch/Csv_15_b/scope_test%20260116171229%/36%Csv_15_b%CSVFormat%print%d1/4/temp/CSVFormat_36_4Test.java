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
        // Use DEFAULT format by default
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue() throws Exception {
        // newRecord true: no delimiter appended
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        invokePrint(null, value, offset, len, out, newRecord);

        // Should append value only, no delimiter
        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws Exception {
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        invokePrint(null, value, offset, len, out, newRecord);

        // Should append delimiter then value
        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws Exception {
        // Create CSVFormat with quote character set
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        out = mock(Appendable.class);
        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        // Spy on CSVFormat to verify printAndQuote is called
        CSVFormat spyFormat = spy(format);

        // Use reflection to get private printAndQuote method
        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        // Use reflection to get private print method on spyFormat
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Instead of mocking printAndQuote directly (private), stub it via doAnswer with reflection
        doAnswer(invocation -> {
            // Do nothing
            return null;
        }).when(spyFormat).printAndQuote(any(), any(), anyInt(), anyInt(), any(), anyBoolean());

        printMethod.invoke(spyFormat, "obj", value, offset, len, out, newRecord);

        // Verify printAndQuote called via reflection
        verify(spyFormat).printAndQuote("obj", value, offset, len, out, newRecord);
        verify(out).append(spyFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws Exception {
        // Create CSVFormat with escape character set and no quote character
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        out = mock(Appendable.class);
        CharSequence value = "escapedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        CSVFormat spyFormat = spy(format);

        // Use reflection to get private printAndEscape method
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Mock printAndEscape via doAnswer with reflection
        doAnswer(invocation -> {
            // Do nothing
            return null;
        }).when(spyFormat).printAndEscape(any(), anyInt(), anyInt(), any());

        printMethod.invoke(spyFormat, "obj", value, offset, len, out, newRecord);

        verify(spyFormat).printAndEscape(value, offset, len, out);
        verify(out, never()).append(spyFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Exception {
        // Create CSVFormat with no quote and no escape
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        out = mock(Appendable.class);
        CharSequence value = "plainValue";
        int offset = 2;
        int len = 5;
        boolean newRecord = false;

        CSVFormat spyFormat = spy(format);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyFormat, "obj", value, offset, len, out, newRecord);

        verify(out).append(spyFormat.getDelimiter());
        verify(out).append(value, offset, offset + len);
    }

    private void invokePrint(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }
}