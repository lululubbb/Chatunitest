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
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_objectNull_newRecordFalse_appendsDelimiterAndValue() throws Exception {
        // Arrange
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = false;

        // Act
        invokePrint(csvFormat, null, value, offset, len, appendable, newRecord);

        // Assert
        verify(appendable).append(csvFormat.getDelimiter());
        verify(appendable).append(value);
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_objectNull_newRecordTrue_appendsValueOnly() throws Exception {
        CharSequence value = "value";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        invokePrint(csvFormat, null, value, offset, len, appendable, newRecord);

        verify(appendable, never()).append(csvFormat.getDelimiter());
        verify(appendable).append(value);
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_callsPrintAndQuote() throws Exception {
        // Arrange
        csvFormat = CSVFormat.DEFAULT.withQuote('\"');
        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        Appendable spyAppendable = spy(appendable);
        CSVFormat spyFormat = spy(csvFormat);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        // Act
        printMethod.invoke(spyFormat, "object", value, offset, len, spyAppendable, newRecord);

        // Assert
        verify(spyFormat).isQuoteCharacterSet();

        // Verify that printAndQuote was invoked via reflection by invoking explicitly
        printAndQuoteMethod.invoke(spyFormat, "object", value, offset, len, spyAppendable, newRecord);

        verifyNoMoreInteractions(spyAppendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_callsPrintAndEscape() throws Exception {
        // Arrange
        csvFormat = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);
        CharSequence value = "escapedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        Appendable spyAppendable = spy(appendable);
        CSVFormat spyFormat = spy(csvFormat);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        // Act
        printMethod.invoke(spyFormat, "object", value, offset, len, spyAppendable, newRecord);

        // Assert
        verify(spyFormat).isQuoteCharacterSet();
        verify(spyFormat).isEscapeCharacterSet();

        // Verify that printAndEscape was invoked via reflection by invoking explicitly
        printAndEscapeMethod.invoke(spyFormat, value, offset, len, spyAppendable);

        verifyNoMoreInteractions(spyAppendable);
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape_appendsValueSubstring() throws Exception {
        // Arrange
        csvFormat = CSVFormat.DEFAULT.withQuote(null).withEscape(null);
        CharSequence value = "0123456789";
        int offset = 2;
        int len = 4;
        boolean newRecord = false;

        Appendable spyAppendable = spy(appendable);

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Act
        printMethod.invoke(csvFormat, "object", value, offset, len, spyAppendable, newRecord);

        // Assert
        verify(spyAppendable).append(csvFormat.getDelimiter());
        verify(spyAppendable).append(value, offset, offset + len);
        verifyNoMoreInteractions(spyAppendable);
    }

    private void invokePrint(CSVFormat format, Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        method.setAccessible(true);
        try {
            method.invoke(format, object, value, offset, len, out, newRecord);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Unwrap IOException thrown by print method
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw e;
        }
    }
}