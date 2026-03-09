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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // Use DEFAULT CSVFormat instance for basic tests
        csvFormat = CSVFormat.DEFAULT;
        appendable = new StringBuilder();
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordFalse_appendsDelimiterAndValue() throws Throwable {
        String value = "value";
        boolean newRecord = false;

        invokePrint(null, value, 0, value.length(), appendable, newRecord);

        String expected = csvFormat.getDelimiter() + value;
        assertEquals(expected, appendable.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullObject_newRecordTrue_appendsValueOnly() throws Throwable {
        String value = "value";
        boolean newRecord = true;

        invokePrint(null, value, 0, value.length(), appendable, newRecord);

        assertEquals(value, appendable.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_quoteCharacterSet_invokesPrintAndQuote() throws Throwable {
        // Create CSVFormat with quoteCharacter set
        CSVFormat formatWithQuote = CSVFormat.DEFAULT.withQuote('\"');

        String value = "quotedValue";
        boolean newRecord = true;

        // Use a spy Appendable to verify output
        StringBuilder sb = new StringBuilder();

        // Invoke print with quoteCharacter set
        invokePrint(formatWithQuote, "anyObject", value, 0, value.length(), sb, newRecord);

        // The output must start and end with quote char
        String output = sb.toString();
        assertEquals('\"', output.charAt(0));
        assertEquals('\"', output.charAt(output.length() - 1));
        // The value should be contained inside
        // Because printAndQuote calls printAndEscape or printAndQuote logic internally,
        // output length should be at least value length + 2 quotes
        assertEquals(value, output.substring(1, output.length() - 1));
    }

    @Test
    @Timeout(8000)
    void testPrint_escapeCharacterSet_invokesPrintAndEscape() throws Throwable {
        // Create CSVFormat with escapeCharacter set and no quoteCharacter
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\').withQuote(null);

        String value = "escapeValue";
        boolean newRecord = true;

        StringBuilder sb = new StringBuilder();

        invokePrint(formatWithEscape, null, value, 0, value.length(), sb, newRecord);

        // The output should contain the original value (printAndEscape appends value with escape if needed)
        String output = sb.toString();
        // Since no special characters, output equals value
        assertEquals(value, output);
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape_appendsValueRange() throws Throwable {
        // Create CSVFormat with no quoteCharacter and no escapeCharacter
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);

        String value = "0123456789";
        boolean newRecord = true;

        StringBuilder sb = new StringBuilder();

        // Append a substring (offset 2, len 5) -> substring "23456"
        invokePrint(formatNoQuoteEscape, null, value, 2, 5, sb, newRecord);

        assertEquals("23456", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_appendsDelimiterFirst() throws Throwable {
        CSVFormat formatNoQuoteEscape = CSVFormat.DEFAULT.withQuote(null).withEscape(null);

        String value = "value";
        boolean newRecord = false;

        StringBuilder sb = new StringBuilder();

        invokePrint(formatNoQuoteEscape, null, value, 0, value.length(), sb, newRecord);

        // Should prepend delimiter
        assertEquals(formatNoQuoteEscape.getDelimiter() + value, sb.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_invalidAppendable_throwsIOException() throws Throwable {
        CSVFormat format = CSVFormat.DEFAULT;

        Appendable failingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("fail");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("fail");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("fail");
            }
        };

        Throwable thrown = assertThrows(IOException.class, () -> {
            invokePrint(format, null, "value", 0, 5, failingAppendable, true);
        });

        assertEquals("fail", thrown.getMessage());
    }

    private void invokePrint(CSVFormat csvFormatInstance, Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws Throwable {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(csvFormatInstance, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void invokePrint(Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws Throwable {
        invokePrint(csvFormat, object, value, offset, len, out, newRecord);
    }
}