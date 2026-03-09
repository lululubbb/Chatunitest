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

    @BeforeEach
    void setUp() {
        // Use default CSVFormat instance for testing
        csvFormat = CSVFormat.DEFAULT.withTrim(false).withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull_EmptyOutput() throws Throwable {
        StringBuilder out = new StringBuilder();
        // nullString is null, expect EMPTY output
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        invokePrint(format, null, out, false);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeAll() throws Throwable {
        StringBuilder out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        invokePrint(format, null, out, false);
        // Expect quoted nullString "NULL"
        assertEquals("\"NULL\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeNotAll() throws Throwable {
        StringBuilder out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        invokePrint(format, null, out, false);
        // Expect unquoted nullString
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_NoTrim() throws Throwable {
        StringBuilder out = new StringBuilder();
        CharSequence value = "value";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false).withQuoteMode(QuoteMode.MINIMAL);
        invokePrint(format, value, out, true);
        // Output should contain the value string
        assertTrue(out.length() > 0);
        assertTrue(out.toString().contains("value") || out.toString().contains("\"value\""));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence_WithTrim() throws Throwable {
        StringBuilder out = new StringBuilder();
        Object value = "  trimmed  ";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true).withQuoteMode(QuoteMode.MINIMAL);
        invokePrint(format, value, out, true);
        // Output should contain trimmed value without leading/trailing spaces
        String output = out.toString();
        assertFalse(output.contains("  trimmed  "));
        assertTrue(output.contains("trimmed"));
    }

    @Test
    @Timeout(8000)
    void testPrint_EmptyString_WithTrim() throws Throwable {
        StringBuilder out = new StringBuilder();
        Object value = "";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true).withQuoteMode(QuoteMode.MINIMAL);
        invokePrint(format, value, out, false);
        assertTrue(out.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOException() {
        Appendable out = mock(Appendable.class);
        try {
            doThrow(new IOException("mock IOException")).when(out).append(any(CharSequence.class));
            csvFormat.print("value", out, false);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("mock IOException", e.getMessage());
        }
    }

    // Helper method to invoke private print(Object, CharSequence, int, int, Appendable, boolean)
    private void invokePrint(CSVFormat format, Object value, Appendable out, boolean newRecord) throws Throwable {
        CharSequence charSequence;
        if (value == null) {
            if (format.getNullString() == null) {
                charSequence = "";
            } else {
                if (QuoteMode.ALL == format.getQuoteMode()) {
                    char quoteChar = format.getQuoteCharacter() != null ? format.getQuoteCharacter() : '"';
                    charSequence = quoteChar + format.getNullString() + quoteChar;
                } else {
                    charSequence = format.getNullString();
                }
            }
        } else {
            charSequence = value instanceof CharSequence ? (CharSequence) value : value.toString();
        }
        if (format.getTrim()) {
            charSequence = invokeTrim(format, charSequence);
        }
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(format, value, charSequence, 0, charSequence.length(), out, newRecord);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // unwrap and throw the original exception
            throw e.getCause();
        }
    }

    // Helper method to invoke private trim(CharSequence)
    private CharSequence invokeTrim(CSVFormat format, CharSequence charSequence) throws Throwable {
        Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);
        return (CharSequence) trimMethod.invoke(format, charSequence);
    }
}