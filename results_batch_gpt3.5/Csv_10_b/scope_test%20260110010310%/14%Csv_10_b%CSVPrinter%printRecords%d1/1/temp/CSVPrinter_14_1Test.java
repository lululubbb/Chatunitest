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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_14_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
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
        when(resultSet.getString(1)).thenReturn("a1", "a2");
        when(resultSet.getString(2)).thenReturn("b1", "b2");
        when(resultSet.getString(3)).thenReturn("c1", "c2");

        CSVPrinter spyPrinter = spy(printer);

        // Use doCallRealMethod only for printRecords, since print and println are final and can't be mocked easily
        doCallRealMethod().when(spyPrinter).printRecords(any(ResultSet.class));
        // For print and println, do nothing to allow verification without invoking real method (which is final)
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);
        // For first row
        inOrder.verify(spyPrinter).print("a1");
        inOrder.verify(spyPrinter).print("b1");
        inOrder.verify(spyPrinter).print("c1");
        inOrder.verify(spyPrinter).println();
        // For second row
        inOrder.verify(spyPrinter).print("a2");
        inOrder.verify(spyPrinter).print("b2");
        inOrder.verify(spyPrinter).print("c2");
        inOrder.verify(spyPrinter).println();

        verifyNoMoreInteractions(spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        when(resultSet.next()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        doCallRealMethod().when(spyPrinter).printRecords(any(ResultSet.class));
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, never()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_sqlExceptionOnGetMetaData() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(resultSet));
        assertEquals("metaData error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_sqlExceptionOnNext() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenThrow(new SQLException("next error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(resultSet));
        assertEquals("next error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_sqlExceptionOnGetString() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenThrow(new SQLException("getString error"));

        SQLException thrown = assertThrows(SQLException.class, () -> printer.printRecords(resultSet));
        assertEquals("getString error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ioExceptionFromPrint() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doCallRealMethod().when(spyPrinter).printRecords(any(ResultSet.class));
        doThrow(new IOException("print error")).when(spyPrinter).print("value");
        doNothing().when(spyPrinter).println();

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(resultSet));
        assertEquals("print error", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_ioExceptionFromPrintln() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doCallRealMethod().when(spyPrinter).printRecords(any(ResultSet.class));
        doNothing().when(spyPrinter).print("value");
        doThrow(new IOException("println error")).when(spyPrinter).println();

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(resultSet));
        assertEquals("println error", thrown.getMessage());
    }
}