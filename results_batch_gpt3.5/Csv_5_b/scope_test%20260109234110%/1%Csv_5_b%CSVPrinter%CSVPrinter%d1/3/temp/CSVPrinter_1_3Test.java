package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_3Test {

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
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_callsValidate() {
        verify(format).validate();
    }

    @Test
    @Timeout(8000)
    void testClose_callsOutCloseable() throws Exception {
        AppendableCloseableMock closeableOut = new AppendableCloseableMock();
        CSVPrinter p = new CSVPrinter(closeableOut, format);
        p.close();
        assertTrue(closeableOut.closed);
    }

    @Test
    @Timeout(8000)
    void testClose_outNotCloseable_noException() throws IOException {
        // out is Appendable but not Closeable
        printer.close();
        // no exception expected
    }

    @Test
    @Timeout(8000)
    void testFlush_callsOutFlushable() throws Exception {
        AppendableFlushableMock flushableOut = new AppendableFlushableMock();
        CSVPrinter p = new CSVPrinter(flushableOut, format);
        p.flush();
        assertTrue(flushableOut.flushed);
    }

    @Test
    @Timeout(8000)
    void testFlush_outNotFlushable_noException() throws IOException {
        printer.flush();
        // no exception expected
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue() throws IOException {
        printer.print(null);
        verify(out).append("");
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "value";
        printer.print(value);
        verify(out).append(value);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String val = "val";
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_privateMethod() throws Exception {
        String val = "val";
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_privateMethod() throws Exception {
        String val = "val";
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintComment() throws IOException {
        String comment = "this is a comment";
        printer.printComment(comment);
        verify(out).append("#");
        verify(out).append(comment);
        verify(out).append("\n");
    }

    @Test
    @Timeout(8000)
    void testPrintln() throws IOException {
        printer.println();
        verify(out).append("\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_iterable() throws IOException {
        Iterable<String> values = Arrays.asList("a", "b", "c");
        printer.printRecord(values);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_varargs() throws IOException {
        printer.printRecord("x", "y", "z");
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_iterable() throws IOException {
        Iterable<Iterable<String>> records = Arrays.asList(
            Arrays.asList("a1", "b1"),
            Arrays.asList("a2", "b2"));
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_array() throws IOException {
        Object[] records = {
            new Object[]{"r1c1", "r1c2"},
            new Object[]{"r2c1", "r2c2"}
        };
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true, true, false);
        java.sql.ResultSetMetaData metaData = mock(java.sql.ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(metaData);
        when(rs.getObject(anyInt())).thenReturn("val");
        printer.printRecords(rs);
        verify(rs, atLeastOnce()).next();
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testGetOut() {
        assertSame(out, printer.getOut());
    }

    // Helper classes to mock Appendable + Closeable
    static class AppendableCloseableMock implements Appendable, Closeable {
        boolean closed = false;

        @Override
        public Appendable append(CharSequence csq) {
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Appendable append(char c) {
            return this;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }
    }

    // Helper classes to mock Appendable + Flushable
    static class AppendableFlushableMock implements Appendable, Flushable {
        boolean flushed = false;

        @Override
        public Appendable append(CharSequence csq) {
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) {
            return this;
        }

        @Override
        public Appendable append(char c) {
            return this;
        }

        @Override
        public void flush() throws IOException {
            flushed = true;
        }
    }
}