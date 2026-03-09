package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_14_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows_multipleColumns() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);

        // Simulate 2 rows
        when(rs.next()).thenReturn(true, true, false);

        when(rs.getString(1)).thenReturn("a1", "a2");
        when(rs.getString(2)).thenReturn("b1", "b2");
        when(rs.getString(3)).thenReturn("c1", "c2");

        CSVPrinter spyPrinter = spy(printer);
        // We want to verify calls to print(Object) and println()
        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(spyPrinter);
        // For first row
        inOrder.verify(spyPrinter).print("a1");
        inOrder.verify(spyPrinter).print("b1");
        inOrder.verify(spyPrinter).print("c1");
        inOrder.verify(spyPrinter).println();
        // For second row
        inOrder.verify(spyPrinter).print("a2");
        inOrder.verify(spyPrinter).print("b2");
        inOrder.verify(spyPrinter).print("c2");
        inOrder.verify(spyPrinter).println();

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_noRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);

        // no rows
        when(rs.next()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        // print and println should never be called
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, never()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow_singleColumn() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);

        when(rs.next()).thenReturn(true, false);

        when(rs.getString(1)).thenReturn("onlyValue");

        CSVPrinter spyPrinter = spy(printer);
        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("onlyValue");
        inOrder.verify(spyPrinter).println();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);

        when(rs.next()).thenThrow(new SQLException("DB error"));

        try {
            printer.printRecords(rs);
        } catch (SQLException e) {
            // expected
            assert (e.getMessage().contains("DB error"));
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOExceptionFromPrint() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);

        when(rs.next()).thenReturn(true);
        when(rs.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("IO error")).when(spyPrinter).print(any());

        try {
            spyPrinter.printRecords(rs);
        } catch (IOException e) {
            // expected
            assert (e.getMessage().contains("IO error"));
        }
    }
}