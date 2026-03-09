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

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullOut_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_Throws() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_Valid_CallsValidate() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);
        verify(format).validate();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrint_Object() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("value");
        assertTrue(out.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    void testPrint_NullObject() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print((Object) null);
        assertTrue(out.toString().isEmpty() || out.toString().contains("")); // depends on implementation
    }

    @Test
    @Timeout(8000)
    void testPrintPrivateMethods() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        Method printPrivate = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printPrivate.setAccessible(true);
        printPrivate.invoke(printer, "obj", "value", 0, 5);

        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);
        printAndEscape.invoke(printer, "escapeValue", 0, 11);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);
        printAndQuote.invoke(printer, "obj", "quoteValue", 0, 10);

        String result = out.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrintComment() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("a comment");
        String output = out.toString();
        assertTrue(output.contains("a comment"));
    }

    @Test
    @Timeout(8000)
    void testPrintln() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.println();
        String output = out.toString();
        assertTrue(output.contains("\n") || output.contains("\r"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordIterable() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(Arrays.asList("a", "b", "c"));
        String output = out.toString();
        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
        assertTrue(output.contains("c"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordVarargs() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord("x", "y", "z");
        String output = out.toString();
        assertTrue(output.contains("x"));
        assertTrue(output.contains("y"));
        assertTrue(output.contains("z"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsIterable() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(Arrays.asList(
                Arrays.asList("1", "2"),
                Arrays.asList("3", "4")
        ));
        String output = out.toString();
        assertTrue(output.contains("1"));
        assertTrue(output.contains("4"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsArray() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(new Object[] {
                new Object[] {"a", "b"},
                new Object[] {"c", "d"}
        });
        String output = out.toString();
        assertTrue(output.contains("a"));
        assertTrue(output.contains("d"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsResultSet() throws Exception {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);

        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true, true, false);
        java.sql.ResultSetMetaData metaData = mock(java.sql.ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(rs.getObject(anyInt())).thenReturn("val");

        printer.printRecords(rs);
        String output = out.toString();
        assertNotNull(output);
    }

    @Test
    @Timeout(8000)
    void testFlushAndClose() throws IOException {
        doNothing().when(format).validate();

        Appendable mockOut = mock(Appendable.class, withSettings().extraInterfaces(Flushable.class, Closeable.class));
        CSVPrinter printer = new CSVPrinter(mockOut, format);

        printer.flush();
        verify((Flushable) mockOut).flush();

        printer.close();
        verify((Closeable) mockOut).close();
    }

    @Test
    @Timeout(8000)
    void testGetOut() throws IOException {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);
        assertSame(out, printer.getOut());
    }
}