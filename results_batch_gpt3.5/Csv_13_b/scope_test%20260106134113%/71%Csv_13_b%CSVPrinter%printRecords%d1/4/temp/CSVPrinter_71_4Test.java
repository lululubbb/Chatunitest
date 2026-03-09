package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class CSVPrinter_71_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
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
        verifyNoMoreInteractions(rs);
        verifyNoMoreInteractions(meta);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(rs.next()).thenReturn(true, false);
        when(rs.getObject(1)).thenReturn("val1");
        when(rs.getObject(2)).thenReturn(123);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(spyPrinter).println();

        verify(rs, times(2)).next();
        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs).getObject(1);
        verify(rs).getObject(2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getObject(1)).thenReturn("row1", "row2");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("row1");
        inOrder.verify(spyPrinter).println();
        inOrder.verify(spyPrinter).print("row2");
        inOrder.verify(spyPrinter).println();

        verify(rs, times(3)).next();
        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs, times(2)).getObject(1);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getMetaData()).thenThrow(new SQLException("meta error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(rs));
        assertEquals("meta error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true);
        when(rs.getObject(1)).thenReturn("val");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        // Use doThrow on the spyPrinter's print(Object) method to simulate IOException
        doThrow(new IOException("io error")).when(spyPrinter).print(any());

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(rs));
        assertEquals("io error", thrown.getMessage());
    }
}