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

class CSVPrinter_65_6Test {

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
    void testPrintComment_NoCommentMarkerSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentMarkerSet();
        verifyNoMoreInteractions(format);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SingleLineComment() throws Throwable {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(printer, true);

        String comment = "simple comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n'); // println appends newline (assumed)
        verify(format, times(2)).getCommentMarker();
        verify(format).isCommentMarkerSet();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_SingleLineComment() throws Throwable {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(printer, false);

        String comment = "single line";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // Because newRecord is false, println() is called first
        inOrder.verify(out).append('\n'); // println assumed to append newline
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n'); // println at end
        verify(format, times(2)).getCommentMarker();
        verify(format).isCommentMarkerSet();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_MultilineCommentWithCRLF() throws Throwable {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(printer, true);

        // comment with CRLF and LF line breaks
        String comment = "line1\r\nline2\nline3\r\n";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF triggers println, then comment marker and space
        inOrder.verify(out).append('\n'); // println()
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // LF triggers println, then comment marker and space
        inOrder.verify(out).append('\n'); // println()
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line3
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('3');

        // CRLF triggers println, then comment marker and space
        inOrder.verify(out).append('\n'); // println()
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // Final println call
        inOrder.verify(out).append('\n'); // println()

        verify(format, times(5)).getCommentMarker();
        verify(format).isCommentMarkerSet();
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, value);
    }
}