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

class CSVPrinter_14_1Test {

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
    void testPrintRecords_multipleRows_multipleColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        when(resultSet.next()).thenReturn(true, true, false);

        when(resultSet.getString(1)).thenReturn("a1", "b1");
        when(resultSet.getString(2)).thenReturn("a2", "b2");
        when(resultSet.getString(3)).thenReturn("a3", "b3");

        // Spy printer to verify print and println calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);
        // For first row
        inOrder.verify(spyPrinter).print("a1");
        inOrder.verify(spyPrinter).print("a2");
        inOrder.verify(spyPrinter).print("a3");
        inOrder.verify(spyPrinter).println();

        // For second row
        inOrder.verify(spyPrinter).print("b1");
        inOrder.verify(spyPrinter).print("b2");
        inOrder.verify(spyPrinter).print("b3");
        inOrder.verify(spyPrinter).println();

        verifyNoMoreInteractions(spyPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_zeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);
        when(resultSet.next()).thenReturn(true, false);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // Should call println once for the single row (no columns)
        verify(spyPrinter, times(1)).println();
        verify(spyPrinter, never()).print(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetNextFalseImmediately() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        // No print or println calls expected
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, never()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetGetStringReturnsNull() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getString(1)).thenReturn(null);
        when(resultSet.getString(2)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(resultSet);

        InOrder inOrder = inOrder(spyPrinter);
        // Cast null explicitly to Object to avoid ambiguity
        inOrder.verify(spyPrinter).print((Object) null);
        inOrder.verify(spyPrinter).print("value");
        inOrder.verify(spyPrinter).println();
    }
}