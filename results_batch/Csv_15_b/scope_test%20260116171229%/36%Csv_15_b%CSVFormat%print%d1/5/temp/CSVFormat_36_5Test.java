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
    void testPrint_nullObject_newRecordTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        // Use reflection to access private method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Execute
        printMethod.invoke(csvFormat, null, value, offset, len, out, newRecord);

        // Verify: no delimiter appended because newRecord == true
        verify(out, never()).append(csvFormat.getDelimiter());
        // Verify value appended as whole
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, null, value, offset, len, out, newRecord);

        // Verify delimiter appended because newRecord == false
        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use CSVFormat with quoteCharacter set
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        Appendable appendable = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Spy on CSVFormat to verify printAndQuote called indirectly
        CSVFormat spyFormat = spy(format);
        // We cannot spy private methods directly, but we can verify interactions on out

        printMethod.invoke(spyFormat, "object", value, offset, len, appendable, newRecord);

        // Because newRecord is false, delimiter appended first
        verify(appendable).append(spyFormat.getDelimiter());
        // We cannot verify private printAndQuote call directly, but verify append called at least once after delimiter
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use CSVFormat with escapeCharacter set but no quoteCharacter
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        CharSequence value = "escapeValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        Appendable appendable = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(format, "object", value, offset, len, appendable, newRecord);

        // Because newRecord true, no delimiter appended
        verify(appendable, never()).append(format.getDelimiter());
        // We cannot verify private printAndEscape call directly, but verify append called at least once
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // CSVFormat with no quoteCharacter and no escapeCharacter
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        CharSequence value = "plainValue";
        int offset = 1;
        int len = 4; // "lain"
        boolean newRecord = false;

        Appendable appendable = mock(Appendable.class);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(format, "object", value, offset, len, appendable, newRecord);

        // delimiter appended first because newRecord false
        verify(appendable).append(format.getDelimiter());
        // append called with value substring
        verify(appendable).append(value, offset, offset + len);
    }
}