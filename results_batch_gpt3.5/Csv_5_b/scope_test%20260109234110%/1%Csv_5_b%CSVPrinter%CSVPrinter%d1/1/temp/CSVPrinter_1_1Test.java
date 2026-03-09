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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_1_1Test {

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
    void testConstructor_nullOut_throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throws() {
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
    void testPrintObject_callsPrintAndEscape() throws IOException {
        String value = "value";
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.print(value);
        try {
            Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
            printMethod.setAccessible(true);
            printMethod.invoke(spyPrinter, value, value, 0, value.length());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            fail("Reflection invocation failed");
        }
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_andPrintAndQuote_viaReflection() throws Exception {
        String testValue = "test,value";
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);
        printAndEscape.invoke(printer, testValue, 0, testValue.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);
        printAndQuote.invoke(printer, testValue, testValue, 0, testValue.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintComment() throws IOException {
        String comment = "This is a comment";
        printer.printComment(comment);
        verify(out).append(Constants.COMMENT);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintln() throws IOException {
        printer.println();
        verify(out).append(Constants.CR);
        verify(out).append(Constants.LF);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordIterable() throws IOException {
        Iterable<String> values = Arrays.asList("a", "b", "c");
        printer.printRecord(values);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordVarargs() throws IOException {
        printer.printRecord("x", "y", "z");
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsIterable() throws IOException {
        Iterable<Iterable<String>> records = Arrays.asList(
                Arrays.asList("1", "2"),
                Arrays.asList("3", "4")
        );
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsArray() throws IOException {
        Object[] records = new Object[] {
                new Object[] {"a", "b"},
                new Object[] {"c", "d"}
        };
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsResultSet() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        java.sql.ResultSetMetaData metaData = mock(java.sql.ResultSetMetaData.class);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(rs.getObject(1)).thenReturn("val1", "val2");
        printer.printRecords(rs);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testFlushAndClose() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.flush();
        verify(spyPrinter).flush();

        spyPrinter.close();
        verify(spyPrinter).close();
    }

    @Test
    @Timeout(8000)
    void testGetOut() {
        assertSame(out, printer.getOut());
    }
}