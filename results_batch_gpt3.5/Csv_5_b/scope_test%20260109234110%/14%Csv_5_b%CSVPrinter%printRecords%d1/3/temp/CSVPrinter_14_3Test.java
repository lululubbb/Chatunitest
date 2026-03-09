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
import org.mockito.invocation.InvocationOnMock;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_14_3Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
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

        when(resultSet.getString(1)).thenReturn("val1_row1", "val1_row2");
        when(resultSet.getString(2)).thenReturn("val2_row1", "val2_row2");
        when(resultSet.getString(3)).thenReturn("val3_row1", "val3_row2");

        // Spy on printer to verify print and println calls
        CSVPrinter spyPrinter = spy(printer);

        // Stub print(Object) to do nothing to avoid IOException from Appendable mock
        doNothing().when(spyPrinter).print(any());

        // Stub println() to do nothing as well
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);
        // For each row, print 3 columns then println
        for (int row = 0; row < 2; row++) {
            inOrder.verify(spyPrinter).print(row == 0 ? "val1_row1" : "val1_row2");
            inOrder.verify(spyPrinter).print(row == 0 ? "val2_row1" : "val2_row2");
            inOrder.verify(spyPrinter).print(row == 0 ? "val3_row1" : "val3_row2");
            inOrder.verify(spyPrinter).println();
        }
        inOrder.verifyNoMoreInteractions();
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

        // Stub print(Object) and println() to do nothing
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        // Since column count is 0, print should never be called, only println once
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(resultSet));
        assertEquals("MetaData error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);

        // Stub println() to do nothing
        doNothing().when(spyPrinter).println();

        // Use doCallRealMethod to call real method print(Object)
        doCallRealMethod().when(spyPrinter).print(any());

        // Mock Appendable.append to throw IOException when appending "value"
        doAnswer((InvocationOnMock invocation) -> {
            CharSequence cs = invocation.getArgument(0, CharSequence.class);
            if ("value".contentEquals(cs)) {
                throw new IOException("IO error");
            }
            return invocation.callRealMethod();
        }).when(out).append(any(CharSequence.class));

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(resultSet));
        assertEquals("IO error", thrown.getMessage());
    }
}