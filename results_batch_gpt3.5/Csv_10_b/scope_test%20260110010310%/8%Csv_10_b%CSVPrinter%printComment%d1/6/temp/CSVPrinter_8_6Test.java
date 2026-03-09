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

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_8_6Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = spy(new CSVPrinter(out, format));
        // Mock println() method to append '\n' to out
        doAnswer(invocation -> {
            out.append('\n');
            return null;
        }).when(csvPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        csvPrinter.printComment("ignored comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_NoLineBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');
        setNewRecord(csvPrinter, true);

        csvPrinter.printComment("abc");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        inOrder.verify(out).append('\n');
        verify(format, times(2)).getCommentStart();
        verify(format).isCommentingEnabled();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_LineBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('!');
        setNewRecord(csvPrinter, false);

        csvPrinter.printComment("xyz");

        InOrder inOrder = inOrder(out);
        // println() before comment start
        inOrder.verify(out).append('\n');
        // comment start + space
        inOrder.verify(out).append('!');
        inOrder.verify(out).append(' ');
        // characters
        inOrder.verify(out).append('x');
        inOrder.verify(out).append('y');
        inOrder.verify(out).append('z');
        // println at end
        inOrder.verify(out).append('\n');
        verify(format, times(2)).getCommentStart();
        verify(format).isCommentingEnabled();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_WithCRLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');
        setNewRecord(csvPrinter, true);

        // comment with CRLF inside: "a\r\nb"
        csvPrinter.printComment("a\r\nb");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        // CR followed by LF: skip LF, println, then comment start and space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('\n');
        verify(format, times(3)).getCommentStart();
        verify(format).isCommentingEnabled();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_WithLFOnly() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('>');

        setNewRecord(csvPrinter, true);

        // comment with LF inside: "x\ny"
        csvPrinter.printComment("x\ny");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('>');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('x');
        // LF causes println, comment start + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('>');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('y');
        inOrder.verify(out).append('\n');

        verify(format, times(3)).getCommentStart();
        verify(format).isCommentingEnabled();
    }

    private void setNewRecord(CSVPrinter printer, boolean value) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.setBoolean(printer, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}