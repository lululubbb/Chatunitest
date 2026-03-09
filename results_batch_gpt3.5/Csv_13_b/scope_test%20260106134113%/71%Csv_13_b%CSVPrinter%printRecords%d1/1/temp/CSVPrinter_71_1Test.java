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
import java.lang.reflect.Method;
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
    void testPrintRecords_multipleRows_multipleColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        // Simulate 2 rows in the ResultSet
        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getObject(1)).thenReturn("val11", "val21");
        when(resultSet.getObject(2)).thenReturn("val12", "val22");
        when(resultSet.getObject(3)).thenReturn("val13", "val23");

        // Spy on printer to verify print and println calls
        CSVPrinter spyPrinter = spy(printer);

        // Use reflection to invoke printRecords to avoid calling the real method on spy (which may cause issues)
        try {
            Method method = CSVPrinter.class.getDeclaredMethod("printRecords", ResultSet.class);
            method.setAccessible(true);
            method.invoke(spyPrinter, resultSet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

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
    void testPrintRecords_noRows() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        // No rows
        when(resultSet.next()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        try {
            Method method = CSVPrinter.class.getDeclaredMethod("printRecords", ResultSet.class);
            method.setAccessible(true);
            method.invoke(spyPrinter, resultSet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        // print and println should never be called
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, never()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow_singleColumn() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getObject(1)).thenReturn("onlyValue");

        CSVPrinter spyPrinter = spy(printer);
        try {
            Method method = CSVPrinter.class.getDeclaredMethod("printRecords", ResultSet.class);
            method.setAccessible(true);
            method.invoke(spyPrinter, resultSet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("onlyValue");
        inOrder.verify(spyPrinter).println();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetGetObjectThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenReturn(true);

        when(resultSet.getObject(1)).thenThrow(new SQLException("getObject failed"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "getObject failed".equals(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetNextThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);

        when(resultSet.next()).thenThrow(new SQLException("next failed"));

        SQLException thrown = null;
        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "next failed".equals(thrown.getMessage());
    }
}