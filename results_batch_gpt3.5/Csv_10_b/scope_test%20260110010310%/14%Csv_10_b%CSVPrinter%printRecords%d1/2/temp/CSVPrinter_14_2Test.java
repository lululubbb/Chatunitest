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

class CSVPrinter_14_2Test {

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
    void testPrintRecords_MultipleRows() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        // Simulate 2 rows
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("r1c1", "r2c1");
        when(resultSet.getString(2)).thenReturn("r1c2", "r2c2");
        when(resultSet.getString(3)).thenReturn("r1c3", "r2c3");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);

        // For first row
        inOrder.verify(spyPrinter).print("r1c1");
        inOrder.verify(spyPrinter).print("r1c2");
        inOrder.verify(spyPrinter).print("r1c3");
        inOrder.verify(spyPrinter).println();

        // For second row
        inOrder.verify(spyPrinter).print("r2c1");
        inOrder.verify(spyPrinter).print("r2c2");
        inOrder.verify(spyPrinter).print("r2c3");
        inOrder.verify(spyPrinter).println();

        verifyNoMoreInteractions(spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ZeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);

        // Simulate 1 row
        when(resultSet.next()).thenReturn(true, false);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // Since columnCount=0, print() should never be called, only println()
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ThrowsSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("meta data error"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            // expected
            assert e.getMessage().contains("meta data error");
        } catch (IOException e) {
            assert false : "Unexpected IOException";
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ThrowsIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("val");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("io error")).when(spyPrinter).print("val");

        try {
            spyPrinter.printRecords(resultSet);
            assert false : "Expected IOException was not thrown";
        } catch (IOException e) {
            assert e.getMessage().contains("io error");
        }
    }
}