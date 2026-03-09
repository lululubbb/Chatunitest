package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_5Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class, withSettings().extraInterfaces(Flushable.class));
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertTrue(ex.getMessage().contains("out"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_callsFormatValidate() {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(out, format);
        verify(format).validate();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testClose_callsOutAppend() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.close();
        verify(out).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testFlush_callsOutFlush() throws IOException {
        Flushable flushableOut = (Flushable) out;
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.flush();
        verify(flushableOut).flush();
    }

    @Test
    @Timeout(8000)
    void testPrint_callsPrivatePrintWithCorrectArgs() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        String value = "testValue";

        // Because the private print method is private and not mocked, just call print and verify out.append calls
        printer.print(value);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape_andPrintAndQuote() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        String testString = "escape\"quote";

        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        printAndEscape.invoke(printer, testString, 0, testString.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));

        printAndQuote.invoke(printer, testString, testString, 0, testString.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintComment_callsOutAppend() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        String comment = "This is a comment";
        printer.printComment(comment);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintln_callsOutAppend() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.println();
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordIterable() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        Iterable<String> values = java.util.Arrays.asList("a", "b", "c");
        printer.printRecord(values);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordVarargs() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord("x", "y", "z");
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsIterable() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        Iterable<Iterable<String>> records = java.util.Arrays.asList(
                java.util.Arrays.asList("1", "2"),
                java.util.Arrays.asList("3", "4")
        );
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsArray() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        Object[] records = new Object[] {
                new Object[] {"5", "6"},
                new Object[] {"7", "8"}
        };
        printer.printRecords(records);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordsResultSet() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);

        ResultSet rs = mock(ResultSet.class);
        java.sql.ResultSetMetaData metaData = mock(java.sql.ResultSetMetaData.class);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getObject(1)).thenReturn("val1", "val3");
        when(rs.getObject(2)).thenReturn("val2", "val4");
        when(rs.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        printer.printRecords(rs);

        verify(rs, times(3)).next();
        verify(rs, atLeastOnce()).getObject(anyInt());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsAppendable() {
        CSVPrinter printer = new CSVPrinter(out, format);
        assertSame(out, printer.getOut());
    }
}