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
        verifyNoMoreInteractions(resultSet, metaData, out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(1)).thenReturn("val1");
        when(resultSet.getObject(2)).thenReturn(123);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(resultSet, metaData, spyPrinter);
        inOrder.verify(resultSet).getMetaData();
        inOrder.verify(metaData).getColumnCount();

        inOrder.verify(resultSet).next();
        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(resultSet).getObject(1);
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(resultSet).getObject(2);
        inOrder.verify(spyPrinter).println();

        inOrder.verify(resultSet).next();

        verifyNoMoreInteractions(resultSet, metaData, spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getObject(1)).thenReturn("row1", "row2");

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(resultSet, metaData, spyPrinter);
        inOrder.verify(resultSet).getMetaData();
        inOrder.verify(metaData).getColumnCount();

        inOrder.verify(resultSet).next();
        inOrder.verify(spyPrinter).print("row1");
        inOrder.verify(resultSet).getObject(1);
        inOrder.verify(spyPrinter).println();

        inOrder.verify(resultSet).next();
        inOrder.verify(spyPrinter).print("row2");
        inOrder.verify(resultSet).getObject(1);
        inOrder.verify(spyPrinter).println();

        inOrder.verify(resultSet).next();

        verifyNoMoreInteractions(resultSet, metaData, spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            // expected
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verify(resultSet).getMetaData();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getObject(1)).thenReturn("val");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("io error")).when(spyPrinter).print(any());

        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            // expected
        }

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet).next();
        verify(resultSet).getObject(1);
        verify(spyPrinter).print("val");
    }
}