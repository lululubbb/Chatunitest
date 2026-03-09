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
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        // Correct length parameter: len = substring length, so pass value.length() - offset
        printMethod.invoke(csvFormat, new Object[] {null, value, 0, value.length(), out, true});

        verify(out, never()).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        printMethod.invoke(csvFormat, new Object[] {null, value, 0, value.length(), out, false});

        verify(out).append(csvFormat.getDelimiter());
        verify(out).append(value);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet() throws Exception {
        CSVFormat formatWithQuote = CSVFormat.DEFAULT.withQuote('\"');
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "quotedValue";
        Object object = 123; // Number to test printAndQuote path
        printMethod.invoke(formatWithQuote, new Object[] {object, value, 0, value.length(), out, false});

        verify(out).append(formatWithQuote.getDelimiter());
        verify(out, atLeast(1)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet() throws Exception {
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "escapeValue";
        Object object = new Object();
        printMethod.invoke(formatWithEscape, new Object[] {object, value, 0, value.length(), out, true});

        verify(out, never()).append(formatWithEscape.getDelimiter());
        verify(out, atLeast(1)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Exception {
        CSVFormat formatNoQuoteNoEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "simpleValue";
        Object object = new Object();
        // offset=1, len= value length - offset = 11 - 1 = 10
        int offset = 1;
        int len = value.length() - offset;

        printMethod.invoke(formatNoQuoteNoEscape, new Object[] {object, value, offset, len, out, false});

        verify(out).append(formatNoQuoteNoEscape.getDelimiter());
        verify(out).append(value, offset, offset + len);
        verifyNoMoreInteractions(out);
    }
}