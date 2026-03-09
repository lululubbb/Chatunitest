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

class CSVPrinter_1_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        doNothing().when(format).validate();
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void constructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void constructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void constructor_callsFormatValidate() throws IOException {
        verify(format).validate();
    }

    @Test
    @Timeout(8000)
    void printAndEscape_invokesAppendProperly() throws Throwable {
        String val = "abc";
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void printAndQuote_invokesAppendProperly() throws Throwable {
        String val = "quoted";
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void print_privatePrintOverloads() throws Throwable {
        String val = "val";
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void print_nullValue_printsEmpty() throws IOException {
        printer.print((Object) null);
        verify(out).append("");
    }

    @Test
    @Timeout(8000)
    void print_nonNullValue_printsValue() throws IOException {
        String value = "test";
        printer.print(value);
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void flush_delegatesToOut() throws IOException {
        printer.flush();
        verify(out).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void close_delegatesToOut() throws IOException {
        printer.close();
        verify(out, atLeast(2)).append(any(CharSequence.class));
        if (out instanceof AutoCloseable) {
            try {
                ((AutoCloseable) out).close();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }
}