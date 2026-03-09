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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_8_5Test {

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
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(format, out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SingleLine() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // newRecord=true by default, so println() not called before
        printer.printComment("comment");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : "comment".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_MultiLineWithCRLF() throws IOException, Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('!');

        // Set newRecord to false via reflection to test println() call at start
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // comment with CRLF, LF and CR alone
        String comment = "line1\r\nline2\nline3\rline4";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // First println() call due to newRecord == false
        inOrder.verify(out).append('!');
        inOrder.verify(out).append(' ');
        // line1\r\n -> CR followed by LF, i increments by 1 on CR
        for (char c : "line1".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // println() after CRLF
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('!');
        inOrder.verify(out).append(' ');
        // line2\n
        for (char c : "line2".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // println() after LF
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('!');
        inOrder.verify(out).append(' ');
        // line3\r
        for (char c : "line3".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // println() after CR (no LF follows)
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('!');
        inOrder.verify(out).append(' ');
        // line4
        for (char c : "line4".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // final println()
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_EmptyComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('%');

        printer.printComment("");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('%');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }
}