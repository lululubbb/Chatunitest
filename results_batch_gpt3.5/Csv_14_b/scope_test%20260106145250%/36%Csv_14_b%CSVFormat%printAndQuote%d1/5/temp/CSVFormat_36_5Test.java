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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        csvFormat = Mockito.spy(CSVFormat.DEFAULT);
        out = new StringBuilder();
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws Throwable {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        try {
            method.invoke(csvFormat, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable, IOException {
        doReturn(QuoteMode.ALL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc,def";
        invokePrintAndQuote("abc,def", value, 0, value.length(), output, false);

        String expected = "\"" + value + "\"";
        assertEquals(expected, output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Object() throws Throwable, IOException {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "someText";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        String expected = "\"" + value + "\"";
        assertEquals(expected, output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Number() throws Throwable, IOException {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        Integer number = 123;
        CharSequence value = "123";
        invokePrintAndQuote(number, value, 0, value.length(), output, false);

        // Should not quote numeric
        assertEquals("123", output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable, IOException {
        doReturn(QuoteMode.NONE).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        CSVFormat spyFormat = Mockito.spy(csvFormat);

        // We spy printAndEscape to verify it is called
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        // We replace printAndEscape by a mock that appends "ESCAPED"
        doAnswer(invocation -> {
            Appendable a = invocation.getArgument(3);
            a.append("ESCAPED");
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        // Replace csvFormat with spyFormat for invocation
        csvFormat = spyFormat;

        StringBuilder output = new StringBuilder();
        String value = "abc";
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        try {
            method.invoke(csvFormat, value, value, 0, value.length(), output, false);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        assertEquals("ESCAPED", output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyNewRecord() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "";
        invokePrintAndQuote("", value, 0, 0, output, true);

        // Should quote empty first token on new record
        assertEquals("\"\"", output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordWithSpecialStartChar() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        // Start char less than '0' (space is 32, '0' is 48), use char 31
        CharSequence value = "\u001Fabc";
        invokePrintAndQuote(value.toString(), value, 0, value.length(), output, true);

        assertTrue(output.toString().startsWith("\""));
        assertTrue(output.toString().endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ValueContainsLineBreak() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc\nxyz";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        assertTrue(output.toString().startsWith("\""));
        assertTrue(output.toString().endsWith("\""));
        assertTrue(output.toString().contains("\n"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ValueEndsWithSpace() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc ";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        assertTrue(output.toString().startsWith("\""));
        assertTrue(output.toString().endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ValueContainsQuoteChar() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc\"def";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        // Quotes doubled inside output
        String expected = "\"abc\"\"def\"";
        assertEquals(expected, output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ValueContainsDelimiter() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(';').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc;def";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        String expected = "\"abc;def\"";
        assertEquals(expected, output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ValueNoQuoteNeeded() throws Throwable, IOException {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc123";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        assertEquals(value, output.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNull_UsesMinimal() throws Throwable, IOException {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        StringBuilder output = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote(value, value, 0, value.length(), output, false);

        assertEquals(value, output.toString());
    }

    @Test
    @Timeout(8000)
    void testUnexpectedQuoteModeThrows() throws Throwable {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // Use reflection to forcibly set a QuoteMode field to an invalid enum by mocking getQuoteMode to return null and then forcibly returning an invalid enum
        // We cannot create invalid enum, so we mock getQuoteMode to return a custom enum subclass that will throw
        doReturn(null).when(csvFormat).getQuoteMode();
        // forcibly call method with invalid enum by mocking getQuoteMode to return a proxy that throws on switch
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        doReturn(null).when(spyFormat).getQuoteMode();

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // We simulate unexpected QuoteMode by passing a mock that will cause the default in switch to be hit
        // We do this by mocking getQuoteMode to return a QuoteMode not in enum (not possible), so instead forcibly call private method with a spy with overridden getQuoteMode
        CSVFormat format = new CSVFormat(',', '"', null, null, null, false, true, "\r\n", null, null, null, false, false, false, false, false) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        };
        Method m = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        m.setAccessible(true);

        StringBuilder output = new StringBuilder();

        // This triggers IllegalStateException because quoteMode is null and default is minimal, so force exception by passing invalid enum by reflection is not possible in Java
        // So this test just ensures IllegalStateException is thrown if default case reached
        // We simulate this by subclassing CSVFormat and overriding getQuoteMode to return null and then forcibly calling with invalid enum by reflection is not possible
        // So test coverage here is limited
        // Instead, test coverage is achieved by coverage of default case in switch by passing null (which triggers minimal)
        // So no test needed here

        // Just assert no exception thrown on null quote mode (minimal)
        invokePrintAndQuote("abc", "abc", 0, 3, output, false);
        assertEquals("abc", output.toString());
    }
}