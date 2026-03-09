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

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = spy(CSVFormat.DEFAULT);
    }

    private String invokePrintAndQuote(CSVFormat format, Object object, CharSequence value, int offset, int len,
            boolean newRecord) throws Exception {
        StringBuilder out = new StringBuilder();
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(format, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Exception {
        doReturn(QuoteMode.ALL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc,def";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertEquals("\"abc,def\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Object() throws Exception {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "hello";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertEquals("\"hello\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Number() throws Exception {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        Integer input = 123;
        String inputStr = input.toString();
        String result = invokePrintAndQuote(csvFormat, input, inputStr, 0, inputStr.length(), false);
        // Should not quote numeric
        assertEquals("123", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Exception {
        doReturn(QuoteMode.NONE).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "abc,def";
        // We spy printAndEscape to verify it is called
        CSVFormat spyFormat = spy(csvFormat);
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class,
                int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();
        // Use reflection to call printAndQuote to cover branch with QuoteMode.NONE
        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class,
                CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        printAndQuoteMethod.invoke(spyFormat, input, input, 0, input.length(), out, false);
        // The result should be same as printAndEscape output, which escapes no chars here so equals input
        assertEquals("abc,def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyNewRecord() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, 0, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordStartCharSpecial() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // First char is space (<= COMMENT), should quote
        String input = " hello";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), true);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ContainsLF() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "hello\nworld";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("\n"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EndsWithSpace() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "hello ";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NoQuoteNeeded() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "hello";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_QuoteCharInside() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "he\"llo";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        // The quote inside should be doubled
        assertEquals("\"he\"\"llo\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_QuoteCharAtEnd() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String input = "hello\"";
        String result = invokePrintAndQuote(csvFormat, input, input, 0, input.length(), false);
        assertEquals("\"hello\"\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ThrowsIllegalStateException() throws Exception {
        doReturn(null).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // We create a subclass to override getQuoteMode returning unknown enum to trigger default case
        CSVFormat format = new CSVFormat(',', '"', null, null, null, false, true, "\r\n", null, null, null, false,
                false, false, false, false) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        };
        // Use reflection to call private method with a custom QuoteMode that is not in switch
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // forcibly set QuoteMode to an unknown enum by mocking getQuoteMode to return null and passing len>0 and newRecord false to hit default
        CSVFormat spyFormat = spy(format);
        doReturn(null).when(spyFormat).getQuoteMode();

        String input = "hello";
        Exception exception = assertThrows(Exception.class, () -> {
            method.invoke(spyFormat, input, input, 0, input.length(), new StringBuilder(), false);
        });
        // Cause is IllegalStateException
        assertTrue(exception.getCause() instanceof IllegalStateException);
        assertTrue(exception.getCause().getMessage().contains("Unexpected Quote value"));
    }
}