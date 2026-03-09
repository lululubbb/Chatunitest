package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_5Test {

    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullOut_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, formatMock));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(outMock, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_ValidParameters_CallsValidate() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);
        verify(formatMock, times(1)).validate();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_ObjectValue() throws Exception {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Case: value is CharSequence
        String value = "hello";
        printMethod.invoke(printer, value, value, 0, value.length());
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));

        // Case: value is not CharSequence (Integer)
        Integer intValue = 123;
        CharSequence cs = intValue.toString();
        printMethod.invoke(printer, intValue, cs, 0, cs.length());
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws Exception {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String val = "escapeTest";
        method.invoke(printer, val, 0, val.length());
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws Exception {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String val = "quoteTest";
        method.invoke(printer, val, val, 0, val.length());
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_ObjectValue() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        printer.print("test");
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));

        printer.print(null);
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintComment() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        printer.printComment("comment");
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintln() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        printer.println();
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_Iterable() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        java.util.List<String> list = java.util.Arrays.asList("a", "b", "c");
        printer.printRecord(list);
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_Varargs() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        printer.printRecord("x", "y", "z");
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_Iterable() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        java.util.List<java.util.List<String>> records = java.util.Arrays.asList(
                java.util.Arrays.asList("1", "2"),
                java.util.Arrays.asList("3", "4")
        );
        printer.printRecords(records);
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_Array() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Object[] records = new Object[]{
                new Object[]{"1", "2"},
                new Object[]{"3", "4"}
        };
        printer.printRecords(records);
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ResultSet() throws Exception {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        ResultSet rs = mock(ResultSet.class);
        java.sql.ResultSetMetaData metaData = mock(java.sql.ResultSetMetaData.class);
        when(rs.next()).thenReturn(true, false);
        when(rs.getMetaData()).thenReturn(metaData);
        when(rs.getObject(anyInt())).thenReturn("val");

        printer.printRecords(rs);

        verify(rs, atLeastOnce()).next();
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testGetOut() throws IOException {
        doNothing().when(formatMock).validate();
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);
        assertSame(outMock, printer.getOut());
    }

    @Test
    @Timeout(8000)
    void testFlushAndClose() throws IOException {
        doNothing().when(formatMock).validate();

        Appendable appendable = mock(Appendable.class, withSettings().extraInterfaces(java.io.Flushable.class, java.io.Closeable.class));
        CSVPrinter printer = new CSVPrinter(appendable, formatMock);

        printer.flush();
        verify((java.io.Flushable) appendable, times(1)).flush();

        printer.close();
        verify((java.io.Closeable) appendable, times(1)).close();
    }
}