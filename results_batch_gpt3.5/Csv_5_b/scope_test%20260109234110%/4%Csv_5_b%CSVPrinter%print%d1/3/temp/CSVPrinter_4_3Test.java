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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_4_3Test {

    @Mock
    private Appendable out;
    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        printer.print(null);

        // Since nullString is null, should print empty string
        verify(out).append("");
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        verify(out).append("NULL");
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "abc";
        // We do not mock format.getNullString() because value != null

        printer.print(value);

        verify(out).append("abc");
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws Throwable {
        // Use reflection to invoke private print(Object, CharSequence, int, int)
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Test with null object and empty string
        privatePrint.invoke(printer, null, "", 0, 0);

        // Test with non-null object and string
        privatePrint.invoke(printer, "obj", "obj", 0, 3);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscapeAndQuoteReflection() throws Throwable {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        // Call printAndEscape with empty string
        printAndEscape.invoke(printer, "", 0, 0);

        // Call printAndQuote with some string
        printAndQuote.invoke(printer, "obj", "obj", 0, 3);
    }
}