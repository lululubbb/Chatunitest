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

class CSVPrinter_1_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
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
    void constructor_callsFormatValidate() {
        verify(format, times(1)).validate();
    }

    @Test
    @Timeout(8000)
    void getOut_returnsAppendable() {
        assertSame(out, printer.getOut());
    }

    @Test
    @Timeout(8000)
    void close_callsOutCloseable() throws Exception {
        Appendable closeableOut = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(char c) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            public void close() throws IOException {}
        };
        CSVPrinter p = new CSVPrinter(closeableOut, format);
        p.close();
    }

    @Test
    @Timeout(8000)
    void close_callsOutAppendableWithoutClose() throws IOException {
        // Appendable that is not Closeable: close() should do nothing without exception
        Appendable appendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(char c) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
        };
        CSVPrinter p = new CSVPrinter(appendable, format);
        assertDoesNotThrow(() -> p.close());
    }

    @Test
    @Timeout(8000)
    void flush_callsOutFlushable() throws Exception {
        Appendable flushableOut = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(char c) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            public void flush() throws IOException {}
        };
        CSVPrinter p = new CSVPrinter(flushableOut, format);
        p.flush();
    }

    @Test
    @Timeout(8000)
    void flush_callsOutAppendableWithoutFlush() throws IOException {
        Appendable appendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(char c) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
        };
        CSVPrinter p = new CSVPrinter(appendable, format);
        assertDoesNotThrow(() -> p.flush());
    }

    @Test
    @Timeout(8000)
    void print_nullValue_appendsNull() throws IOException {
        when(out.append("null")).thenReturn(out);
        printer.print(null);
        verify(out).append("null");
    }

    @Test
    @Timeout(8000)
    void print_simpleValue_appendsValue() throws IOException {
        when(out.append("abc")).thenReturn(out);
        printer.print("abc");
        verify(out).append("abc");
    }

    @Test
    @Timeout(8000)
    void printRecord_iterable_callsPrintForEach() throws IOException {
        Iterable<Object> iterable = java.util.Arrays.asList("a", "b", "c");
        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();
        spyPrinter.printRecord(iterable);
        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void printRecord_varargs_callsPrintForEach() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();
        spyPrinter.printRecord("x", "y", "z");
        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void printComment_appendsComment() throws IOException {
        String comment = "This is a comment";
        when(out.append('#')).thenReturn(out);
        when(out.append(comment)).thenReturn(out);
        when(out.append(System.lineSeparator())).thenReturn(out);
        printer.printComment(comment);
        verify(out).append('#');
        verify(out).append(comment);
        verify(out).append(System.lineSeparator());
    }

    @Test
    @Timeout(8000)
    void println_appendsNewLine() throws IOException {
        when(out.append('\r')).thenReturn(out);
        when(out.append('\n')).thenReturn(out);
        printer.println();
        verify(out).append('\r');
        verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void printRecords_iterable_callsPrintRecordForEach() throws IOException {
        Iterable<Iterable<?>> records = java.util.Arrays.asList(
                java.util.Arrays.asList("a", "b"),
                java.util.Arrays.asList("c", "d")
        );
        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).printRecord(any(Iterable.class));
        spyPrinter.printRecords(records);
        verify(spyPrinter, times(2)).printRecord(any(Iterable.class));
    }

    @Test
    @Timeout(8000)
    void printRecords_varargs_callsPrintRecordForEach() throws IOException {
        Object[] records = new Object[] {
                new Object[] {"a", "b"},
                new Object[] {"c", "d"}
        };
        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).printRecord(any(Object[].class));
        spyPrinter.printRecords(records);
        verify(spyPrinter, times(2)).printRecord(any(Object[].class));
    }

    @Test
    @Timeout(8000)
    void printAndEscape_privateMethod_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String val = "escapeTest";
        when(out.append(any(CharSequence.class))).thenReturn(out);
        method.invoke(printer, val, 0, val.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void printAndQuote_privateMethod_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String val = "quoteTest";
        when(out.append(any(CharSequence.class))).thenReturn(out);
        method.invoke(printer, val, val, 0, val.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void print_privateMethod_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        method.setAccessible(true);
        String val = "printTest";
        when(out.append(any(CharSequence.class))).thenReturn(out);
        method.invoke(printer, val);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}