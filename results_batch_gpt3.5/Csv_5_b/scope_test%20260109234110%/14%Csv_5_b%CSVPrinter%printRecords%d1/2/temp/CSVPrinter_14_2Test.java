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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_14_2Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSetMetaData metaData;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws SQLException {
        mocks = MockitoAnnotations.openMocks(this);
        when(resultSet.getMetaData()).thenReturn(metaData);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows_multipleColumns() throws SQLException, IOException {
        when(metaData.getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("a1", "b1");
        when(resultSet.getString(2)).thenReturn("a2", "b2");
        when(resultSet.getString(3)).thenReturn("a3", "b3");

        printer = spy(printer);
        doNothing().when(printer).print(any());
        doNothing().when(printer).println();

        printer.printRecords(resultSet);

        InOrder inOrder = inOrder(printer);
        // First row prints
        inOrder.verify(printer).print("a1");
        inOrder.verify(printer).print("a2");
        inOrder.verify(printer).print("a3");
        inOrder.verify(printer).println();
        // Second row prints
        inOrder.verify(printer).print("b1");
        inOrder.verify(printer).print("b2");
        inOrder.verify(printer).print("b3");
        inOrder.verify(printer).println();

        verify(resultSet, times(3)).next();
        verify(resultSet, times(2)).getMetaData();
        verify(metaData, times(1)).getColumnCount();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_noRows() throws SQLException, IOException {
        when(metaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(false);

        printer = spy(printer);
        doNothing().when(printer).print(any());
        doNothing().when(printer).println();

        printer.printRecords(resultSet);

        verify(printer, never()).print(any());
        verify(printer, never()).println();
        verify(resultSet, times(1)).next();
        verify(metaData, times(1)).getColumnCount();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow_singleColumn() throws SQLException, IOException {
        when(metaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("only");

        printer = spy(printer);
        doNothing().when(printer).print(any());
        doNothing().when(printer).println();

        printer.printRecords(resultSet);

        InOrder inOrder = inOrder(printer);
        inOrder.verify(printer).print("only");
        inOrder.verify(printer).println();

        verify(resultSet, times(2)).next();
        verify(metaData, times(1)).getColumnCount();
    }
}