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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_14_6Test {

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
    void testPrintRecords_emptyResultSet() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);
        when(rs.next()).thenReturn(false);

        printer.printRecords(rs);

        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs).next();
        verifyNoMoreInteractions(rs, meta, out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRowMultipleColumns() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString(1)).thenReturn("val1");
        when(rs.getString(2)).thenReturn("val2");
        when(rs.getString(3)).thenReturn("val3");

        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(rs, meta, spyPrinter);
        inOrder.verify(rs).getMetaData();
        inOrder.verify(meta).getColumnCount();
        inOrder.verify(rs).next();

        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(spyPrinter).print("val2");
        inOrder.verify(spyPrinter).print("val3");
        inOrder.verify(spyPrinter).println();

        inOrder.verify(rs).next();

        verifyNoMoreInteractions(rs, meta, spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString(1)).thenReturn("r1c1", "r2c1");
        when(rs.getString(2)).thenReturn("r1c2", "r2c2");

        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(rs, meta, spyPrinter);
        inOrder.verify(rs).getMetaData();
        inOrder.verify(meta).getColumnCount();

        // first row
        inOrder.verify(rs).next();
        inOrder.verify(spyPrinter).print("r1c1");
        inOrder.verify(spyPrinter).print("r1c2");
        inOrder.verify(spyPrinter).println();

        // second row
        inOrder.verify(rs).next();
        inOrder.verify(spyPrinter).print("r2c1");
        inOrder.verify(spyPrinter).print("r2c2");
        inOrder.verify(spyPrinter).println();

        // end
        inOrder.verify(rs).next();

        verifyNoMoreInteractions(rs, meta, spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetThrowsSQLException() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenThrow(new SQLException("SQL error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(rs));
        assertEquals("SQL error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetGetStringThrowsSQLException() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true);
        when(rs.getString(1)).thenThrow(new SQLException("SQL getString error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(rs));
        assertEquals("SQL getString error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_printThrowsIOException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("IO error")).when(spyPrinter).print("value");
        doNothing().when(spyPrinter).println();

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(rs));
        assertEquals("IO error", thrown.getMessage());
    }
}