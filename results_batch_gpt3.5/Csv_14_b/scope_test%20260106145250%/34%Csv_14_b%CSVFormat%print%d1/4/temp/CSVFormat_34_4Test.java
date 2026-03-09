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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable mockAppendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        mockAppendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse_appendsDelimiterAndValue() throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        boolean newRecord = false;

        printMethod.invoke(csvFormat, null, value, 0, value.length(), mockAppendable, newRecord);

        // verify delimiter appended first
        verify(mockAppendable).append(csvFormat.getDelimiter());
        // verify value appended
        verify(mockAppendable).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue_appendsValueOnly() throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        boolean newRecord = true;

        printMethod.invoke(csvFormat, null, value, 0, value.length(), mockAppendable, newRecord);

        // verify delimiter NOT appended
        verify(mockAppendable, never()).append(csvFormat.getDelimiter());
        // verify value appended
        verify(mockAppendable).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_callsPrintAndQuote() throws Throwable {
        // Use CSVFormat with quote character set (DEFAULT has quote char)
        CSVFormat formatWithQuote = CSVFormat.DEFAULT;

        Appendable appendable = mock(Appendable.class);
        CharSequence value = "quotedValue";
        Object object = 123; // non-null object
        boolean newRecord = false;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Spy on CSVFormat to verify printAndQuote call
        CSVFormat spyFormat = spy(formatWithQuote);

        // We cannot spy private method printAndQuote directly, so we verify interaction by mocking printAndQuote
        // Using doNothing for printAndQuote to avoid IOException
        doNothing().when(spyFormat).printAndQuote(any(), any(), anyInt(), anyInt(), any(), anyBoolean());

        printMethod.invoke(spyFormat, object, value, 0, value.length(), appendable, newRecord);

        verify(appendable).append(spyFormat.getDelimiter());
        verify(spyFormat).printAndQuote(object, value, 0, value.length(), appendable, newRecord);
        verify(spyFormat, never()).printAndEscape(any(), anyInt(), anyInt(), any());
        verify(appendable, never()).append(value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_callsPrintAndEscape() throws Throwable {
        // Create CSVFormat with escape character set but no quote character
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withQuote(null).withEscape('\\');

        Appendable appendable = mock(Appendable.class);
        CharSequence value = "escapedValue";
        Object object = new Object(); // non-null object
        boolean newRecord = true;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        CSVFormat spyFormat = spy(formatWithEscape);

        doNothing().when(spyFormat).printAndEscape(any(), anyInt(), anyInt(), any());

        printMethod.invoke(spyFormat, object, value, 0, value.length(), appendable, newRecord);

        verify(spyFormat).printAndEscape(value, 0, value.length(), appendable);
        verify(spyFormat, never()).printAndQuote(any(), any(), anyInt(), anyInt(), any(), anyBoolean());
        verify(appendable, never()).append(value, 0, value.length());
        verify(appendable, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_neitherQuoteNorEscape_appendsValueSubstring() throws Throwable {
        // Create CSVFormat with no quote and no escape character
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);

        Appendable appendable = mock(Appendable.class);
        CharSequence value = "substringValue";
        Object object = new Object(); // non-null object
        boolean newRecord = false;

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(formatNoQuoteEscape, object, value, 3, 6, appendable, newRecord);

        // Should append delimiter first since newRecord is false
        verify(appendable).append(formatNoQuoteEscape.getDelimiter());
        // Should append substring value from offset 3 to 9 (3 + 6)
        verify(appendable).append(value, 3, 9);
    }

    @Test
    @Timeout(8000)
    void testPrint_IOExceptionThrown_propagates() throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Appendable appendable = mock(Appendable.class);
        doThrow(new IOException("test exception")).when(appendable).append(anyChar());

        CharSequence value = "value";
        boolean newRecord = false;

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                printMethod.invoke(csvFormat, null, value, 0, value.length(), appendable, newRecord);
            } catch (Exception e) {
                // unwrap InvocationTargetException
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                }
                throw e;
            }
        });

        assertEquals("test exception", thrown.getMessage());
    }
}