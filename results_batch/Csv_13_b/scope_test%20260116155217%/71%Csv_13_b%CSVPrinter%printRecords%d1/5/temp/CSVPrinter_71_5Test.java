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

class CSVPrinter_71_5Test {

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
    void testPrintRecords_emptyResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);
        when(resultSet.next()).thenReturn(false);

        printer.printRecords(resultSet);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet).next();
        verifyNoMoreInteractions(resultSet, metaData, out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRowsMultipleColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        // Simulate two rows
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getObject(1)).thenReturn("val11", "val21");
        when(resultSet.getObject(2)).thenReturn("val12", "val22");
        when(resultSet.getObject(3)).thenReturn("val13", "val23");

        CSVPrinter spyPrinter = spy(printer);

        // Use doCallRealMethod() only on print(Object) and println(), 
        // but disable real printRecords to avoid recursive calls
        doCallRealMethod().when(spyPrinter).print(any());
        doCallRealMethod().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);

        // First row prints
        inOrder.verify(spyPrinter).print("val11");
        inOrder.verify(spyPrinter).print("val12");
        inOrder.verify(spyPrinter).print("val13");
        inOrder.verify(spyPrinter).println();

        // Second row prints
        inOrder.verify(spyPrinter).print("val21");
        inOrder.verify(spyPrinter).print("val22");
        inOrder.verify(spyPrinter).print("val23");
        inOrder.verify(spyPrinter).println();

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet, times(3)).next();

        verify(resultSet, times(2)).getObject(1);
        verify(resultSet, times(2)).getObject(2);
        verify(resultSet, times(2)).getObject(3);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("MetaData failure"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "MetaData failure".equals(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetNextThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenThrow(new SQLException("next failure"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "next failure".equals(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_getObjectThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenThrow(new SQLException("getObject failure"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "getObject failure".equals(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_printThrowsIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("print failure")).when(spyPrinter).print("value");

        IOException thrown = null;
        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "print failure".equals(thrown.getMessage());
    }
}