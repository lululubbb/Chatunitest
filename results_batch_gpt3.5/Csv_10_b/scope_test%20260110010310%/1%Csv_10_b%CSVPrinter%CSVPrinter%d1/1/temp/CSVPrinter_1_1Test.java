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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_1Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, format);
        });
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(out, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_callsFormatValidate() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);
        verify(format, times(1)).validate();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_invokesAppend() throws Exception {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        Method m = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        m.setAccessible(true);

        CharSequence value = "escapeTest";

        when(out.append(any(CharSequence.class))).thenReturn(out);

        m.invoke(printer, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_invokesAppend() throws Exception {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        Method m = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        m.setAccessible(true);

        CharSequence value = "quoteTest";

        when(out.append(any(CharSequence.class))).thenReturn(out);

        m.invoke(printer, "obj", value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethod() throws Exception {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        Method m = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        m.setAccessible(true);

        CharSequence value = "printTest";

        when(out.append(any(CharSequence.class))).thenReturn(out);

        m.invoke(printer, "obj", value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_publicPrintMethod_callsPrivatePrint() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        when(out.append(any(CharSequence.class))).thenReturn(out);

        printer.print("value");

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsAppendable() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);
        assertSame(out, printer.getOut());
    }
}