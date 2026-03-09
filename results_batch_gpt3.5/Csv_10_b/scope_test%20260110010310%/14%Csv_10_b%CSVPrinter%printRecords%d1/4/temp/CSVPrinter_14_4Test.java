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

class CSVPrinter_14_4Test {

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
    void testPrintRecords_withMultipleRowsAndColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        // Simulate 2 rows
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("r1c1", "r2c1");
        when(resultSet.getString(2)).thenReturn("r1c2", "r2c2");
        when(resultSet.getString(3)).thenReturn("r1c3", "r2c3");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Use doCallRealMethod for print(Object) and println() to call real methods
        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);
        // For row 1
        inOrder.verify(spyPrinter).print("r1c1");
        inOrder.verify(spyPrinter).print("r1c2");
        inOrder.verify(spyPrinter).print("r1c3");
        inOrder.verify(spyPrinter).println();
        // For row 2
        inOrder.verify(spyPrinter).print("r2c1");
        inOrder.verify(spyPrinter).print("r2c2");
        inOrder.verify(spyPrinter).print("r2c3");
        inOrder.verify(spyPrinter).println();

        verifyNoMoreInteractions(spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withZeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);

        // Simulate 1 row only
        when(resultSet.next()).thenReturn(true, false);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        // Since columnCount=0, print should never be called, but println should be called once
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        assertThrows(SQLException.class, () -> printer.printRecords(resultSet));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIOExceptionFromPrint() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        // Use doThrow on the real print(Object) method with argument "value"
        doThrow(new IOException("print error")).when(spyPrinter).print("value");

        IOException ex = assertThrows(IOException.class, () -> spyPrinter.printRecords(resultSet));
        assertEquals("print error", ex.getMessage());
    }
}