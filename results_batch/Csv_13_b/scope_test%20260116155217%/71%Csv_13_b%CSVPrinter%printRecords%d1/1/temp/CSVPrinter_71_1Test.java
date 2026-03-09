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

class CSVPrinter_71_1Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

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

        // Simulate two rows
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getObject(1)).thenReturn("val11", "val21");
        when(resultSet.getObject(2)).thenReturn("val12", "val22");
        when(resultSet.getObject(3)).thenReturn("val13", "val23");

        // Spy on printer to verify print and println calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);

        // For first row
        inOrder.verify(spyPrinter).print("val11");
        inOrder.verify(spyPrinter).print("val12");
        inOrder.verify(spyPrinter).print("val13");
        inOrder.verify(spyPrinter).println();

        // For second row
        inOrder.verify(spyPrinter).print("val21");
        inOrder.verify(spyPrinter).print("val22");
        inOrder.verify(spyPrinter).print("val23");
        inOrder.verify(spyPrinter).println();

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withZeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);
        when(resultSet.next()).thenReturn(true, false);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // Since columnCount is 0, print should never be called, only println once
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("io error")).when(spyPrinter).print("value");

        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            // Expected
        }
    }
}