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

class CSVPrinter_71_2Test {

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

        // Setup getObject for each column in each row
        when(resultSet.getObject(1)).thenReturn("a1", "b1");
        when(resultSet.getObject(2)).thenReturn("a2", "b2");
        when(resultSet.getObject(3)).thenReturn("a3", "b3");

        // Spy on printer to verify internal print and println calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // Verify print called for each column value in each row
        InOrder inOrder = inOrder(spyPrinter);
        // First row prints
        inOrder.verify(spyPrinter).print("a1");
        inOrder.verify(spyPrinter).print("a2");
        inOrder.verify(spyPrinter).print("a3");
        inOrder.verify(spyPrinter).println();
        // Second row prints
        inOrder.verify(spyPrinter).print("b1");
        inOrder.verify(spyPrinter).print("b2");
        inOrder.verify(spyPrinter).print("b3");
        inOrder.verify(spyPrinter).println();

        // Verify no more interactions
        verifyNoMoreInteractions(spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withZeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);

        // Simulate 1 row
        when(resultSet.next()).thenReturn(true, false);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // Since zero columns, print should never be called, but println should be called once
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("print error")).when(spyPrinter).print("value");

        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            // expected
        }
    }
}