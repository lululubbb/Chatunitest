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

class CSVPrinter_71_6Test {

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
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(false);

        printer.printRecords(resultSet);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet).next();
        verifyNoMoreInteractions(resultSet);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow_multipleColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        // Simulate one row then end
        when(resultSet.next()).thenReturn(true, false);

        // Return values for each column
        when(resultSet.getObject(1)).thenReturn("val1");
        when(resultSet.getObject(2)).thenReturn(2);
        when(resultSet.getObject(3)).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);

        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(spyPrinter).print(2);
        inOrder.verify(spyPrinter).print(null);
        inOrder.verify(spyPrinter).println();

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet, times(2)).next();
        verify(resultSet).getObject(1);
        verify(resultSet).getObject(2);
        verify(resultSet).getObject(3);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows_multipleColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        // Simulate three rows then end
        when(resultSet.next()).thenReturn(true, true, true, false);

        when(resultSet.getObject(1)).thenReturn("r1c1", "r2c1", "r3c1");
        when(resultSet.getObject(2)).thenReturn("r1c2", "r2c2", "r3c2");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);

        // Row 1
        inOrder.verify(spyPrinter).print("r1c1");
        inOrder.verify(spyPrinter).print("r1c2");
        inOrder.verify(spyPrinter).println();

        // Row 2
        inOrder.verify(spyPrinter).print("r2c1");
        inOrder.verify(spyPrinter).print("r2c2");
        inOrder.verify(spyPrinter).println();

        // Row 3
        inOrder.verify(spyPrinter).print("r3c1");
        inOrder.verify(spyPrinter).print("r3c2");
        inOrder.verify(spyPrinter).println();

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet, times(4)).next();

        verify(resultSet, times(3)).getObject(1);
        verify(resultSet, times(3)).getObject(2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData failure"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            assert e.getMessage().contains("metaData failure");
        } catch (IOException e) {
            assert false : "IOException not expected";
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

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doThrow(new IOException("io failure")).when(spyPrinter).print(eq("value"));

        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            assert e.getMessage().contains("io failure");
        }
    }
}