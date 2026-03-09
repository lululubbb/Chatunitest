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

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        csvFormat = spy(CSVFormat.DEFAULT);
        out = new StringBuilder();
    }

    private void setQuoteMode(QuoteMode mode) {
        doReturn(mode).when(csvFormat).getQuoteMode();
    }

    private void setQuoteCharacter(Character ch) {
        doReturn(ch).when(csvFormat).getQuoteCharacter();
    }

    private void setDelimiter(char delim) {
        doReturn(delim).when(csvFormat).getDelimiter();
    }

    private String invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out,
            boolean newRecord) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Exception {
        setQuoteMode(QuoteMode.ALL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "value";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"value\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Object() throws Exception {
        setQuoteMode(QuoteMode.NON_NUMERIC);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "abc123";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("abc", input, 0, input.length(), outBuilder, false);
        assertEquals("\"abc123\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Number() throws Exception {
        setQuoteMode(QuoteMode.NON_NUMERIC);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "12345";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote(12345, input, 0, input.length(), outBuilder, false);
        // Number should not be quoted
        assertEquals("12345", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Exception {
        setQuoteMode(QuoteMode.NONE);
        setQuoteCharacter('"');
        setDelimiter(',');
        // We spy printAndEscape to verify it is called
        CSVFormat spyFormat = spy(csvFormat);
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        Appendable appendable = new StringBuilder();
        // We mock printAndEscape to write something to appendable
        doAnswer(invocation -> {
            CharSequence val = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            Appendable out = invocation.getArgument(3);
            out.append(val, offset, offset + len);
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        method.invoke(spyFormat, "abc", "abc", 0, 3, appendable, false);
        assertEquals("abc", appendable.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyNewRecord() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("","", 0, 0, outBuilder, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordFirstCharSpecial() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "#value";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, true);
        assertEquals("\"#value\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordFirstCharControlChar() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "\u001Fvalue";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, true);
        assertEquals("\"\u001Fvalue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsLineBreak() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "val\nue";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"val\nue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsCR() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "val\rue";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"val\rue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsQuoteChar() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "val\"ue";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"val\"\"ue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsDelimiter() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "val,ue";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"val,ue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_endsWithControlChar() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "value\u001F";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("\"value\u001F\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_noQuoteNeeded() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "value";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value", input, 0, input.length(), outBuilder, false);
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_quoteCharAtEnd() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "value\"";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("value\"", input, 0, input.length(), outBuilder, false);
        assertEquals("\"value\"\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_multipleQuoteChar() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "\"val\"ue\"";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("\"val\"ue\"", input, 0, input.length(), outBuilder, false);
        assertEquals("\"\"\"val\"\"ue\"\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyNotNewRecord() throws Exception {
        setQuoteMode(QuoteMode.MINIMAL);
        setQuoteCharacter('"');
        setDelimiter(',');
        String input = "";
        StringBuilder outBuilder = new StringBuilder();
        String result = invokePrintAndQuote("", input, 0, 0, outBuilder, false);
        // empty and not new record, no quote needed
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeUnexpected_throws() throws Exception {
        setQuoteCharacter('"');
        setDelimiter(',');
        // Set a mock QuoteMode that is not in the switch (simulate unexpected)
        doReturn(null).when(csvFormat).getQuoteMode();
        // forcibly set to an invalid enum by reflection
        doReturn(new QuoteMode() { }).when(csvFormat).getQuoteMode();

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        Exception ex = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(csvFormat, "value", "value", 0, 5, new StringBuilder(), false);
        });
        assertTrue(ex.getCause() instanceof IllegalStateException);
        assertTrue(ex.getCause().getMessage().contains("Unexpected Quote value"));
    }
}