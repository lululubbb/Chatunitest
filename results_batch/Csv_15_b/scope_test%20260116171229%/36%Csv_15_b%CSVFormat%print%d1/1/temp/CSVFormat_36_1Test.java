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

    private CSVFormat csvFormatNoQuoteNoEscape;
    private CSVFormat csvFormatWithQuote;
    private CSVFormat csvFormatWithEscape;

    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // CSVFormat with no quote and no escape characters set
        csvFormatNoQuoteNoEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        // CSVFormat with quote character set
        csvFormatWithQuote = CSVFormat.DEFAULT.withQuote('"').withEscape(null);
        // CSVFormat with escape character set but no quote character
        csvFormatWithEscape = CSVFormat.DEFAULT.withQuote(null).withEscape('\\');

        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse() throws Throwable {
        // invoke print with object == null and newRecord == false
        invokePrint(csvFormatNoQuoteNoEscape, null, "value", 0, 5, appendable, false);

        // verify delimiter appended first
        verify(appendable).append(csvFormatNoQuoteNoEscape.getDelimiter());
        // then value appended fully
        verify(appendable).append("value");
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue() throws Throwable {
        // invoke print with object == null and newRecord == true (no delimiter)
        invokePrint(csvFormatNoQuoteNoEscape, null, "value", 0, 5, appendable, true);

        // verify delimiter not appended
        verify(appendable, never()).append(csvFormatNoQuoteNoEscape.getDelimiter());
        // value appended fully
        verify(appendable).append("value");
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_newRecordFalse() throws Throwable {
        // For quote character set, printAndQuote is called.
        // We'll spy the CSVFormat to verify printAndQuote is called.

        CSVFormat spyFormat = spy(csvFormatWithQuote);

        // Stub printAndQuote to just append "quoted" string for verification
        doAnswer(invocation -> {
            Appendable out = invocation.getArgument(4);
            out.append("quoted");
            return null;
        }).when(spyFormat).printAndQuote(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        invokePrint(spyFormat, "obj", "value", 0, 5, appendable, false);

        // verify delimiter appended first
        verify(appendable).append(spyFormat.getDelimiter());
        // verify printAndQuote called with correct parameters
        verify(spyFormat).printAndQuote(eq("obj"), eq("value"), eq(0), eq(5), eq(appendable), eq(false));
        // verify append "quoted" was called inside printAndQuote stub
        verify(appendable).append("quoted");
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_newRecordTrue() throws Throwable {
        CSVFormat spyFormat = spy(csvFormatWithQuote);

        doAnswer(invocation -> {
            Appendable out = invocation.getArgument(4);
            out.append("quoted");
            return null;
        }).when(spyFormat).printAndQuote(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        invokePrint(spyFormat, "obj", "value", 0, 5, appendable, true);

        // no delimiter appended for newRecord == true
        verify(appendable, never()).append(spyFormat.getDelimiter());
        verify(spyFormat).printAndQuote(eq("obj"), eq("value"), eq(0), eq(5), eq(appendable), eq(true));
        verify(appendable).append("quoted");
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_noQuote_newRecordFalse() throws Throwable {
        CSVFormat spyFormat = spy(csvFormatWithEscape);

        // Stub printAndEscape to append "escaped"
        doAnswer(invocation -> {
            Appendable out = invocation.getArgument(3);
            out.append("escaped");
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        invokePrint(spyFormat, "obj", "value", 0, 5, appendable, false);

        verify(appendable).append(spyFormat.getDelimiter());
        verify(spyFormat).printAndEscape(eq("value"), eq(0), eq(5), eq(appendable));
        verify(appendable).append("escaped");
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_noQuote_newRecordTrue() throws Throwable {
        CSVFormat spyFormat = spy(csvFormatWithEscape);

        doAnswer(invocation -> {
            Appendable out = invocation.getArgument(3);
            out.append("escaped");
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        invokePrint(spyFormat, "obj", "value", 0, 5, appendable, true);

        verify(appendable, never()).append(spyFormat.getDelimiter());
        verify(spyFormat).printAndEscape(eq("value"), eq(0), eq(5), eq(appendable));
        verify(appendable).append("escaped");
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape_newRecordFalse() throws Throwable {
        invokePrint(csvFormatNoQuoteNoEscape, "obj", "value", 1, 3, appendable, false);

        verify(appendable).append(csvFormatNoQuoteNoEscape.getDelimiter());
        // append with offset and len: append(value, offset, offset+len)
        verify(appendable).append("value", 1, 4);
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape_newRecordTrue() throws Throwable {
        invokePrint(csvFormatNoQuoteNoEscape, "obj", "value", 1, 3, appendable, true);

        verify(appendable, never()).append(csvFormatNoQuoteNoEscape.getDelimiter());
        verify(appendable).append("value", 1, 4);
    }

    private void invokePrint(CSVFormat format, Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord) throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(format, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}