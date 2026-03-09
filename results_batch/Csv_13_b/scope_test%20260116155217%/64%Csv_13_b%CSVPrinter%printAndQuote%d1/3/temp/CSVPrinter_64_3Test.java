package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintAndQuoteTest {

    private CSVPrinter csvPrinter;
    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        csvFormat = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, csvFormat);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(csvPrinter, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        // Expect quote char appended at start
        verify(out).append('"');
        // Expect value appended fully with quote doubling logic: no quote chars inside so one append with full value
        verify(out).append(value, 0, 3);
        // Expect quote char appended at end
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Object() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, 3);
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_Number() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "123";
        Number numberObject = 123;

        // Since object is Number, quote = false, so append directly
        invokePrintAndQuote(numberObject, value, 0, value.length());

        verify(out).append(value, 0, 3);
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "abc";

        // We mock printAndEscape to verify it is called indirectly
        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        doNothing().when(spyPrinter).printAndEscape(value, 0, value.length());

        method.invoke(spyPrinter, "abc", value, 0, value.length());

        verify(spyPrinter).printAndEscape(value, 0, value.length());
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyLen_NewRecordTrue() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "";

        // newRecord is private boolean default true, so no change needed

        invokePrintAndQuote("abc", value, 0, 0);

        verify(out).append('"');
        verify(out).append(value, 0, 0);
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordTrue_SpecialStartChar() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Value starting with a char < '0' to trigger quote true on newRecord
        CharSequence value = "#abc";

        // newRecord true by default

        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordFalse_ContainsLF() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to false via reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "ab\nc";

        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordFalse_EndsWithSpace() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to false via reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc ";

        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append('"');
        verify(out).append(value, 0, value.length());
        verify(out).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NewRecordFalse_NoQuoteNeeded() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Set newRecord to false via reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(csvPrinter, false);

        CharSequence value = "abc";

        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append(value, 0, value.length());
        verify(out, never()).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeUnexpected_ThrowsIllegalStateException() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(null);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Force an unexpected QuoteMode by mocking getQuoteMode to return a dummy enum
        QuoteMode dummyQuoteMode = mock(QuoteMode.class);
        when(csvFormat.getQuoteMode()).thenReturn(dummyQuoteMode);
        when(dummyQuoteMode.toString()).thenReturn("DUMMY");

        // We override csvFormat.getQuoteMode() to return dummyQuoteMode, but the switch expects known enums
        // So we forcibly call the private method and expect IllegalStateException

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            method.invoke(csvPrinter, "abc", "abc", 0, 3);
        });

        assertTrue(ex.getMessage().contains("Unexpected Quote value"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll_WithQuoteCharInsideValue() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        CharSequence value = "a\"bc\"d";

        invokePrintAndQuote("abc", value, 0, value.length());

        verify(out).append('"');

        // The value contains two quote chars at positions 1 and 4, so append called multiple times
        // The method appends from start to pos+1 each time a quoteChar is found and resets start

        // The expected calls:
        // append(value, 0, 2) for "a\""
        // append(value, 1, 5) for "\"bc\""
        // append(value, 4, 6) for "\"d"
        // then append('"')

        // We verify these calls in order
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('"');
        inOrder.verify(out).append(value, 0, 2);
        inOrder.verify(out).append(value, 1, 5);
        inOrder.verify(out).append(value, 4, 6);
        inOrder.verify(out).append('"');
    }
}