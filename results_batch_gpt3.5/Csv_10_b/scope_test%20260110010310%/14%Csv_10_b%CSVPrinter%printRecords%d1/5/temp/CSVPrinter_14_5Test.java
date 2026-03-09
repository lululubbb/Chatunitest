package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;

class CSVPrinter_14_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class); // format is not used in printRecords logic directly
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

        // No output expected because no rows
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_singleRow() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString(1)).thenReturn("val1");
        when(rs.getString(2)).thenReturn("val2");

        CSVPrinter spyPrinter = spy(printer);

        // Stub print(Object) and println() to do nothing to avoid actual output
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter).print("val1");
        verify(spyPrinter).print("val2");
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_multipleRows() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(1);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString(1)).thenReturn("row1", "row2");

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter).print("row1");
        verify(spyPrinter).print("row2");
        verify(spyPrinter, times(2)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullValues() throws SQLException, IOException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);

        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(rs.next()).thenReturn(true, false);
        when(rs.getString(1)).thenReturn(null);
        when(rs.getString(2)).thenReturn("value");

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecords(rs);

        // Use eq(null) instead of isNull() to avoid compilation issues
        verify(spyPrinter).print((Object) null);
        verify(spyPrinter).print("value");
        verify(spyPrinter).println();
    }

}