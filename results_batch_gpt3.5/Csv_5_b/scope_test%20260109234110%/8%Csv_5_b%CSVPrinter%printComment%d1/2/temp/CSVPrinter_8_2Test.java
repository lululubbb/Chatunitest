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

class CSVPrinter_8_2Test {

    private CSVPrinter printer;
    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentingDisabled() throws IOException {
        when(formatMock.isCommentingEnabled()).thenReturn(false);

        printer.printComment("any comment");

        verify(formatMock).isCommentingEnabled();
        verifyNoMoreInteractions(formatMock);
        verify(outMock, never()).append(any());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SimpleComment() throws IOException {
        when(formatMock.isCommentingEnabled()).thenReturn(true);
        when(formatMock.getCommentStart()).thenReturn('#');

        // newRecord is true by default, so println() should NOT be called before
        printer.printComment("abc");

        InOrder inOrder = inOrder(outMock);
        // append comment start char
        inOrder.verify(outMock).append('#');
        // append space
        inOrder.verify(outMock).append(' ');

        // append chars 'a','b','c'
        inOrder.verify(outMock).append('a');
        inOrder.verify(outMock).append('b');
        inOrder.verify(outMock).append('c');

        verify(formatMock).isCommentingEnabled();
        verify(formatMock, times(1)).getCommentStart();
        verifyNoMoreInteractions(formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_CallsPrintlnBefore() throws Exception {
        when(formatMock.isCommentingEnabled()).thenReturn(true);
        when(formatMock.getCommentStart()).thenReturn('#');

        // set private field newRecord to false via reflection
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.setBoolean(printer, false);

        // spy on printer to verify println() call
        CSVPrinter spyPrinter = spy(printer);

        // call printComment on spy
        spyPrinter.printComment("x");

        // verify println() called once before printing comment start
        InOrder inOrder = inOrder(spyPrinter, outMock);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        inOrder.verify(outMock).append('x');
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentWithCRLF() throws IOException {
        when(formatMock.isCommentingEnabled()).thenReturn(true);
        when(formatMock.getCommentStart()).thenReturn('#');

        String comment = "a\r\nb";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        // 'a'
        inOrder.verify(outMock).append('a');
        // CR + LF triggers println() and append comment start + space again
        inOrder.verify(outMock).append('\n');
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        // 'b'
        inOrder.verify(outMock).append('b');
        // final println()
        inOrder.verify(outMock).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentWithCRWithoutLF() throws IOException {
        when(formatMock.isCommentingEnabled()).thenReturn(true);
        when(formatMock.getCommentStart()).thenReturn('#');

        String comment = "a\rb";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        // 'a'
        inOrder.verify(outMock).append('a');
        // CR without LF triggers println() and append comment start + space again
        inOrder.verify(outMock).append('\n');
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        // 'b'
        inOrder.verify(outMock).append('b');
        // final println()
        inOrder.verify(outMock).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentWithLF() throws IOException {
        when(formatMock.isCommentingEnabled()).thenReturn(true);
        when(formatMock.getCommentStart()).thenReturn('#');

        String comment = "a\nb";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        // 'a'
        inOrder.verify(outMock).append('a');
        // LF triggers println() and append comment start + space again
        inOrder.verify(outMock).append('\n');
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        // 'b'
        inOrder.verify(outMock).append('b');
        // final println()
        inOrder.verify(outMock).append('\n');
    }
}