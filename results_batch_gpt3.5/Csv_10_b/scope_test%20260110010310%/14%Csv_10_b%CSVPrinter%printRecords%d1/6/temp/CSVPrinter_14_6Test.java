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

class CSVPrinter_14_6Test {

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
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);
        when(rs.next()).thenReturn(false);

        printer.printRecords(rs);

        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs).next();
        verifyNoMoreInteractions(rs, meta, out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRowMultipleColumns() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(3);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString(1)).thenReturn("val1");
        when(rs.getString(2)).thenReturn("val2");
        when(rs.getString(3)).thenReturn("val3");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs, times(2)).next();
        verify(rs).getString(1);
        verify(rs).getString(2);
        verify(rs).getString(3);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("val1");
        inOrder.verify(spyPrinter).print("val2");
        inOrder.verify(spyPrinter).print("val3");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString(1)).thenReturn("r1c1", "r2c1");
        when(rs.getString(2)).thenReturn("r1c2", "r2c2");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        verify(rs).getMetaData();
        verify(meta).getColumnCount();
        verify(rs, times(3)).next();

        verify(rs, times(2)).getString(1);
        verify(rs, times(2)).getString(2);

        InOrder inOrder = inOrder(spyPrinter);
        // First row prints
        inOrder.verify(spyPrinter).print("r1c1");
        inOrder.verify(spyPrinter).print("r1c2");
        inOrder.verify(spyPrinter).println();
        // Second row prints
        inOrder.verify(spyPrinter).print("r2c1");
        inOrder.verify(spyPrinter).print("r2c2");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetThrowsSQLException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenThrow(new SQLException("next failed"));

        SQLException thrown = null;
        try {
            printer.printRecords(rs);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert thrown.getMessage().contains("next failed");
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_resultSetGetStringThrowsSQLException() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true);
        when(rs.getString(1)).thenThrow(new SQLException("getString failed"));

        SQLException thrown = null;
        try {
            printer.printRecords(rs);
        } catch (SQLException e) {
            thrown = e;
        }
        assert thrown != null;
        assert thrown.getMessage().contains("getString failed");
    }
}