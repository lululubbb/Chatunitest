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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_65_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        printer = new CSVPrinter(out, format);

        // Set newRecord field accessible and true by default
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentMarkerNotSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);
        printer.printComment("any comment");
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordTrue_simpleComment() throws IOException {
        // newRecord is true, so no println() call before printing comment marker
        printer.printComment("simple comment");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : "simple comment".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append(System.lineSeparator()); // println() appends line separator
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordFalse_callsPrintlnBefore() throws Exception {
        // set newRecord to false to test println() call before printing comment marker
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("comment");

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('c');
        inOrder.verify(out).append('o');
        inOrder.verify(out).append('m');
        inOrder.verify(out).append('m');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('t');
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentWithCRLF() throws IOException {
        String comment = "line1\r\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF triggers println() and prints comment marker + space again
        inOrder.verify(out).append(System.lineSeparator());
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println()
        inOrder.verify(out).append(System.lineSeparator());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentWithLF() throws IOException {
        String comment = "line1\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // LF triggers println() and prints comment marker + space again
        inOrder.verify(out).append(System.lineSeparator());
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println()
        inOrder.verify(out).append(System.lineSeparator());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_emptyComment() throws IOException {
        printer.printComment("");
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append(System.lineSeparator());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_printlnThrowsIOException() throws Exception {
        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("println failed")).when(spyPrinter).println();

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printComment("abc"));
        assertEquals("println failed", thrown.getMessage());
    }
}