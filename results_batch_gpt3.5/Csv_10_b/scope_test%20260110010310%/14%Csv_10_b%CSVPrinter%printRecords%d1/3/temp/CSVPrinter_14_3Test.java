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
import org.mockito.Mockito;

class CSVPrinter_14_3Test {

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

        // Simulate two rows
        when(resultSet.next()).thenReturn(true, true, false);

        // Provide values for columns in row 1 and row 2
        when(resultSet.getString(1)).thenReturn("val11", "val21");
        when(resultSet.getString(2)).thenReturn("val12", "val22");
        when(resultSet.getString(3)).thenReturn("val13", "val23");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(resultSet);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();

        verify(resultSet, times(3)).next();

        verify(resultSet, times(6)).getString(anyInt());

        InOrder inOrder = inOrder(spyPrinter);
        // row 1
        inOrder.verify(spyPrinter).print("val11");
        inOrder.verify(spyPrinter).print("val12");
        inOrder.verify(spyPrinter).print("val13");
        inOrder.verify(spyPrinter).println();
        // row 2
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

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(resultSet);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();

        verify(resultSet, times(2)).next();

        verify(spyPrinter, never()).print(any());

        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("meta"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "meta".equals(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOExceptionFromPrint() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doThrow(new IOException("io-exception")).when(spyPrinter).print("value");

        IOException thrown = null;
        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "io-exception".equals(thrown.getMessage());
    }
}