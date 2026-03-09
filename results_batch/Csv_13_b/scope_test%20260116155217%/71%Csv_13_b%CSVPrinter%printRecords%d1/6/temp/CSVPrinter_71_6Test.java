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
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);
        when(rs.next()).thenReturn(false);

        printer.printRecords(rs);

        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs).next();
        verifyNoMoreInteractions(rs);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(rs.next()).thenReturn(true, false);
        when(rs.getObject(1)).thenReturn("value1");
        when(rs.getObject(2)).thenReturn(42);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(rs, meta, spyPrinter);
        inOrder.verify(rs).getMetaData();
        inOrder.verify(meta).getColumnCount();
        inOrder.verify(rs).next();
        inOrder.verify(spyPrinter).print("value1");
        inOrder.verify(spyPrinter).print(42);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(rs).next();

        verifyNoMoreInteractions(rs);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getObject(1)).thenReturn("r1", "r2");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(rs);

        InOrder inOrder = inOrder(rs, meta, spyPrinter);
        inOrder.verify(rs).getMetaData();
        inOrder.verify(meta).getColumnCount();

        inOrder.verify(rs).next();
        inOrder.verify(spyPrinter).print("r1");
        inOrder.verify(spyPrinter).println();

        inOrder.verify(rs).next();
        inOrder.verify(spyPrinter).print("r2");
        inOrder.verify(spyPrinter).println();

        inOrder.verify(rs).next();

        verifyNoMoreInteractions(rs);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsSQLException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getMetaData()).thenThrow(new SQLException("meta failed"));

        try {
            printer.printRecords(rs);
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_throwsIOException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true);
        when(rs.getObject(1)).thenReturn("value");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doThrow(new IOException("io error")).when(spyPrinter).print("value");

        try {
            spyPrinter.printRecords(rs);
        } catch (IOException e) {
            // expected
        }
    }
}