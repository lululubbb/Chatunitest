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

class CSVPrinter_71_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyResultSet() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(false);

        csvPrinter.printRecords(resultSet);

        verify(resultSet).getMetaData();
        verify(metaData).getColumnCount();
        verify(resultSet).next();
        verifyNoMoreInteractions(resultSet);
        verify(out, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);

        // Simulate two rows
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getObject(1)).thenReturn("val1_row1", "val1_row2");
        when(resultSet.getObject(2)).thenReturn("val2_row1", "val2_row2");

        CSVPrinter spyPrinter = spy(csvPrinter);

        // Spy print(Object) and println() to avoid actual Appendable calls
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(resultSet, spyPrinter);
        inOrder.verify(resultSet).getMetaData();
        inOrder.verify(metaData).getColumnCount();

        // First row
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getObject(1);
        inOrder.verify(spyPrinter).print("val1_row1");
        inOrder.verify(resultSet).getObject(2);
        inOrder.verify(spyPrinter).print("val2_row1");
        inOrder.verify(spyPrinter).println();

        // Second row
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getObject(1);
        inOrder.verify(spyPrinter).print("val1_row2");
        inOrder.verify(resultSet).getObject(2);
        inOrder.verify(spyPrinter).print("val2_row2");
        inOrder.verify(spyPrinter).println();

        // End iteration
        inOrder.verify(resultSet).next();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetThrowsSQLException() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> csvPrinter.printRecords(resultSet));
        assertEquals("metaData error", thrown.getMessage());
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

        CSVPrinter spyPrinter = spy(csvPrinter);
        doThrow(new IOException("print error")).when(spyPrinter).print("value");

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(resultSet));
        assertEquals("print error", thrown.getMessage());
    }
}