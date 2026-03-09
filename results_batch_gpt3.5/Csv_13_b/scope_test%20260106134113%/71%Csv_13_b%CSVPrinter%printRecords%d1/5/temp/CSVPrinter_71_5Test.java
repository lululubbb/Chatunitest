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
import org.mockito.stubbing.Answer;

class CSVPrinter_71_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = spy(new CSVPrinter(out, format));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMultipleRowsAndColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        class ResultSetMock {
            private int nextCount = 0;

            boolean next() {
                nextCount++;
                return nextCount <= 2;
            }

            Object getObject(int columnIndex) {
                if (nextCount == 1) {
                    switch (columnIndex) {
                        case 1: return "val11";
                        case 2: return "val12";
                        case 3: return "val13";
                        default: return null;
                    }
                } else if (nextCount == 2) {
                    switch (columnIndex) {
                        case 1: return "val21";
                        case 2: return "val22";
                        case 3: return "val23";
                        default: return null;
                    }
                }
                return null;
            }
        }
        final ResultSetMock rsMock = new ResultSetMock();

        reset(resultSet);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);

        when(resultSet.next()).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) {
                return rsMock.next();
            }
        });

        when(resultSet.getObject(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object arg = invocation.getArguments()[0];
                int col = (arg instanceof Integer) ? (Integer) arg : 0;
                return rsMock.getObject(col);
            }
        });

        printer.printRecords(resultSet);

        InOrder inOrder = inOrder(printer);
        inOrder.verify(printer).print("val11");
        inOrder.verify(printer).print("val12");
        inOrder.verify(printer).print("val13");
        inOrder.verify(printer).println();
        inOrder.verify(printer).print("val21");
        inOrder.verify(printer).print("val22");
        inOrder.verify(printer).print("val23");
        inOrder.verify(printer).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withZeroColumns() throws SQLException, IOException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);

        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(0);
        when(resultSet.next()).thenReturn(true, false);

        printer.printRecords(resultSet);

        verify(printer, times(1)).println();
        verify(printer, never()).print(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("meta error"));

        try {
            printer.printRecords(resultSet);
        } catch (SQLException e) {
            // expected
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        doThrow(new IOException("io error")).when(spyPrinter).print(any());

        try {
            spyPrinter.printRecords(resultSet);
        } catch (IOException e) {
            // expected
        }
    }
}