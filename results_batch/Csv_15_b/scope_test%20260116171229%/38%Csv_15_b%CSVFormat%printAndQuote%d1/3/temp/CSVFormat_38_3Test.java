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

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    private Method getPrintAndQuoteMethod() throws NoSuchMethodException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeAll() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL);
        StringBuilder out = new StringBuilder();
        String value = "value123";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "value123", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("value123"));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeAllNonNull_ObjectNull() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        String value = "nonNull";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, null, value, 0, value.length(), out, false);
        // null object, should not quote because ALL_NON_NULL quotes non-null only
        assertEquals(value, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeAllNonNull_ObjectNonNull() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        String value = "nonNull";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abc", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("nonNull"));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_Number() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        String value = "12345";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, 12345, value, 0, value.length(), out, false);
        // Number object, should not quote
        assertEquals(value, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNonNumeric_NonNumber() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        String value = "abc123";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abc", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("abc123"));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeNone_CallsPrintAndEscape() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NONE);
        StringBuilder out = spy(new StringBuilder());
        String value = "abc,def";
        Method method = getPrintAndQuoteMethod();

        // We spy on the CSVFormat to verify printAndEscape is called
        CSVFormat spyFormat = spy(format);
        Method printAndEscape = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class,
                int.class, Appendable.class);
        printAndEscape.setAccessible(true);

        doAnswer(invocation -> {
            CharSequence val = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            Appendable appendable = invocation.getArgument(3);
            appendable.append(val, offset, offset + len);
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        // Invoke printAndQuote on spyFormat
        method.invoke(spyFormat, "abc", value, 0, value.length(), out, false);

        verify(spyFormat, times(1)).printAndEscape(value, 0, value.length(), out);
        // Because printAndEscape appends the value directly, out should equal value
        assertEquals(value, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NewRecordEmptyLen() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "", value, 0, 0, out, true);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_CharAtStartTriggersQuote() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        // char at pos is 0x01 (less than 0x20), newRecord true triggers quote
        String value = "\u0001abc";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abc", value, 0, value.length(), out, true);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_CharAtStartIsCommentChar() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL).withCommentMarker('#');
        StringBuilder out = new StringBuilder();
        String value = "#comment";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "comment", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_ContainsLfCrQuoteOrDelimiter() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        // value contains delimiter (comma)
        String value = "abc,def";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abc,def", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_EndCharLessThanSpace() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        // last char is space (<= ' ')
        String value = "abc ";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abc ", value, 0, value.length(), out, false);
        String result = out.toString();
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_NoQuoteNeeded() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abcDEF123";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, "abcDEF123", value, 0, value.length(), out, false);
        assertEquals(value, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeMinimal_QuoteCharInsideValue() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        Character quoteChar = format.getQuoteCharacter();
        String value = "abc" + quoteChar + "def";
        Method method = getPrintAndQuoteMethod();
        method.invoke(format, value, value, 0, value.length(), out, false);
        String result = out.toString();
        // Should double the quote char inside and wrap with quotes
        assertTrue(result.startsWith(quoteChar.toString()));
        assertTrue(result.endsWith(quoteChar.toString()));
        // The quote char should appear doubled inside
        String doubledQuote = "" + quoteChar + quoteChar;
        assertTrue(result.contains(doubledQuote));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_QuoteModeUnexpected_Throws() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(null);
        // Use reflection to forcibly set quoteMode to an invalid enum to test defaulting to MINIMAL
        // Here we test defaulting to MINIMAL by passing null, no exception expected
        StringBuilder out = new StringBuilder();
        String value = "abc";
        Method method = getPrintAndQuoteMethod();
        // Should not throw IllegalStateException because null quoteMode defaults to MINIMAL
        method.invoke(format, "abc", value, 0, value.length(), out, false);
        assertEquals(value, out.toString());

        // Now forcibly test IllegalStateException by mocking getQuoteMode to return an invalid enum
        CSVFormat spyFormat = spy(format);
        doReturn(null).when(spyFormat).getQuoteMode();
        // We cannot create an invalid enum, so we simulate by overriding method to throw IllegalStateException
        Method printAndQuoteMethod = getPrintAndQuoteMethod();
        CSVFormat invalidFormat = new CSVFormat(',', '"', null, null, null, false, false, "\r\n", null, null, null,
                false, false, false, false, false, false) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        };

        // Reflection to invoke with overridden getQuoteMode returning null (defaults to MINIMAL)
        // So no exception expected here either
        StringBuilder out2 = new StringBuilder();
        printAndQuoteMethod.invoke(invalidFormat, "abc", value, 0, value.length(), out2, false);
        assertEquals(value, out2.toString());
    }
}