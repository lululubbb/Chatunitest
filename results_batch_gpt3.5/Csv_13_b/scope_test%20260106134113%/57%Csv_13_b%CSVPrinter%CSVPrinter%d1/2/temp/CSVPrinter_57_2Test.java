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

class CSVPrinter_57_2Test {

    private StringBuilder out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullOut() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullFormat() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructorWithHeaderCommentsAndHeaderPrinted() throws IOException {
        String[] headerComments = new String[] {"comment1", null, "comment2"};
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);
        String output = out.toString();

        assertTrue(output.contains("comment1"));
        assertTrue(output.contains("comment2"));
        assertTrue(output.contains("h1"));
        assertTrue(output.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNoHeaderCommentsAndSkipHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);
        String output = out.toString();

        assertFalse(output.contains("h1"));
        assertFalse(output.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNoHeaderAndNoHeaderComments() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintCommentMethod() throws Exception {
        when(format.getHeaderComments()).thenReturn(new String[0]);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        Method printCommentMethod = CSVPrinter.class.getDeclaredMethod("printComment", String.class);
        printCommentMethod.setAccessible(true);

        printCommentMethod.invoke(printer, "myComment");
        assertTrue(out.toString().contains("myComment"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeAndPrintAndQuote() throws Exception {
        when(format.getHeaderComments()).thenReturn(new String[0]);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
        printAndEscapeMethod.invoke(printer, "escapeTest", 0, "escapeTest".length());
        String outStr = out.toString();
        assertFalse(outStr.isEmpty());

        out.setLength(0);
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
        printAndQuoteMethod.invoke(printer, "obj", "quoteTest", 0, "quoteTest".length());
        assertTrue(out.toString().contains("quoteTest"));
    }

    @Test
    @Timeout(8000)
    void testCloseFlushGetOut() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        Appendable mockOut = mock(Appendable.class, withSettings().extraInterfaces(java.io.Flushable.class, java.io.Closeable.class));
        CSVPrinter printer = new CSVPrinter(mockOut, format);

        // flush and close should delegate to out if possible
        printer.flush();
        verify((java.io.Flushable) mockOut).flush();

        printer.close();
        verify((java.io.Closeable) mockOut).close();

        assertSame(mockOut, printer.getOut());
    }
}