package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_5Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullOut_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, format);
        });
        assertEquals("out", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullFormat_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(out, null);
        });
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        String[] headerComments = new String[]{"comment1", "comment2"};
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[]{"header1", "header2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        assertTrue(result.contains("comment1"));
        assertTrue(result.contains("comment2"));
        assertTrue(result.contains("header1"));
        assertTrue(result.contains("header2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullHeader_skipsHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withSkipHeaderRecord_true_skipsHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[]{"header1"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        assertFalse(out.toString().contains("header1"));
    }

    @Test
    @Timeout(8000)
    void testPrintComment_invokesPrintComment() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("test comment");
        String result = out.toString();
        assertTrue(result.contains("test comment"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_varargs_printsValues() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord("value1", "value2");
        String result = out.toString();
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_iterable_printsValues() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(Arrays.asList("val1", "val2"));
        String result = out.toString();
        assertTrue(result.contains("val1"));
        assertTrue(result.contains("val2"));
    }
}