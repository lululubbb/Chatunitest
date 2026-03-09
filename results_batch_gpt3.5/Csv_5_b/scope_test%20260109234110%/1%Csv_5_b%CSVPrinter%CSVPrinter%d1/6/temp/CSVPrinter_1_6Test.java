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

class CSVPrinter_1_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class, withSettings().extraInterfaces(Closeable.class, Flushable.class));
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
    void testClose_callsOutCloseIfPossible() throws IOException {
        doNothing().when((Closeable) out).close();
        printer.close();
        verify((Closeable) out).close();
    }

    @Test
    @Timeout(8000)
    void testFlush_callsOutFlushIfPossible() throws IOException {
        doNothing().when((Flushable) out).flush();
        printer.flush();
        verify((Flushable) out).flush();
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue() throws IOException {
        printer.print((Object) null);
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "test";
        printer.print(value);
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, "abc,def", 0, 7);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                fail("IOException thrown: " + e.getCause().getMessage());
            } else {
                throw e;
            }
        }
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, "obj", "quoted", 0, 6);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                fail("IOException thrown: " + e.getCause().getMessage());
            } else {
                throw e;
            }
        }
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrint_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, "obj", "value", 0, 5);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                fail("IOException thrown: " + e.getCause().getMessage());
            } else {
                throw e;
            }
        }
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintln() throws IOException {
        printer.println();
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordIterable() throws IOException {
        printer.printRecord(Arrays.asList("one", "two", "three"));
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordVarargs() throws IOException {
        printer.printRecord("one", "two", "three");
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsIterable() throws IOException {
        printer.printRecords(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c", "d")));
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsArray() throws IOException {
        Object[] records = new Object[] { new Object[] { "a", "b" }, new Object[] { "c", "d" } };
        printer.printRecords(records);
        verifyAppendCalled();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsResultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true, false);
        java.sql.ResultSetMetaData meta = mock(java.sql.ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.getObject(1)).thenReturn("value");
        printer.printRecords(rs);
        verifyAppendCalled();
        verify(rs).close();
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsAppendable() {
        assertSame(out, printer.getOut());
    }

    private void verifyAppendCalled() {
        try {
            verify(out, atLeastOnce()).append(any(CharSequence.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}