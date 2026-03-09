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

class CSVPrinter_8_4Test {

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
    void testPrintComment_commentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        // Should return immediately and not append anything
        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordTrue_singleLineComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // newRecord is true by default, so println() not called before
        printer.printComment("hello");

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).isCommentingEnabled();
        inOrder.verify(format).getCommentStart();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('h');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('o');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordFalse_printlnCalledBefore() throws IOException, Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('!');

        // Set newRecord to false via reflection
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Spy on printer to verify println() call
        CSVPrinter spyPrinter = spy(printer);

        // Stub format calls on spyPrinter's format field (same as original)
        // Because spyPrinter uses the same format and out as printer, mocks are valid

        // Call printComment on spy to verify println() call
        spyPrinter.printComment("test");

        verify(spyPrinter).println();
        verify(format).getCommentStart();
        verify(out).append('!');
        verify(out).append(' ');
        verify(out).append('t');
        verify(out).append('e');
        verify(out).append('s');
        verify(out).append('t');
        verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentWithCRLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn(';');

        String comment = "line1\r\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).isCommentingEnabled();
        inOrder.verify(format, times(2)).getCommentStart();
        inOrder.verify(out).append(';');
        inOrder.verify(out).append(' ');
        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');
        // CRLF triggers println + commentStart + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append(';');
        inOrder.verify(out).append(' ');
        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        // final println
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentWithCROnly() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('%');

        String comment = "a\rb";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).isCommentingEnabled();
        inOrder.verify(format, times(2)).getCommentStart();
        inOrder.verify(out).append('%');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        // CR triggers println + commentStart + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('%');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentWithLFOnly() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('@');

        String comment = "x\ny";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).isCommentingEnabled();
        inOrder.verify(format, times(2)).getCommentStart();
        inOrder.verify(out).append('@');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('x');
        // LF triggers println + commentStart + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('@');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('y');
        inOrder.verify(out).append('\n');
    }
}