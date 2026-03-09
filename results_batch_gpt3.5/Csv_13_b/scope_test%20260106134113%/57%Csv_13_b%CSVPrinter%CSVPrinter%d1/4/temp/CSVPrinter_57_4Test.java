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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_4Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader() throws Exception {
        String[] headerComments = new String[] {"comment1", null, "comment2"};
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[] {"col1", "col2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String output = out.toString();
        assertTrue(output.contains("comment1"));
        assertTrue(output.contains("comment2"));
        assertTrue(output.contains("col1"));
        assertTrue(output.contains("col2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderCommentsAndSkipHeader() throws Exception {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[] {"col1"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        String output = out.toString();
        assertFalse(output.contains("col1"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullHeaderAndNullHeaderComments() throws Exception {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String output = out.toString();
        assertEquals("", output);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        Exception ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        Exception ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testInvokePrivatePrintComment() throws Exception {
        String[] headerComments = new String[] {"comment"};
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        Method printComment = CSVPrinter.class.getDeclaredMethod("printComment", String.class);
        printComment.setAccessible(true);
        printComment.invoke(printer, "myComment");

        assertTrue(out.toString().contains("myComment"));
    }

    @Test
    @Timeout(8000)
    void testInvokePrivatePrint() throws Exception {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        // Pass the substring of "value" as CharSequence with offset 0 and length 5
        printMethod.invoke(printer, "obj", "value", 0, 5);

        String output = out.toString();
        assertTrue(output.length() > 0);
    }
}