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

class CSVPrinter_8_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);
        // Should return immediately, no append calls
        printer.printComment("any comment");
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_NoNewlineBefore() throws IOException {
        // newRecord is true by default
        printer.printComment("abc");
        InOrder inOrder = inOrder(out);

        // Should append comment start and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // Each character appended
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');

        // Finally println() called which appends newline
        // println() appends line separator, which is platform dependent, so we can't verify exact append
        // But we can verify that append was called at least once more (for println)
        verify(out, atLeast(1)).append(anyChar());

        // No more interactions
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_ShouldPrintlnBefore() throws IOException {
        // Set newRecord to false via reflection
        setNewRecord(printer, false);

        // Spy on printer to verify println() call
        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("abc");

        // Verify println() called once before printing comment
        InOrder inOrderPrinter = inOrder(spyPrinter);
        inOrderPrinter.verify(spyPrinter).println();

        // Verify append calls after println
        Appendable spyOut = spyPrinter.getOut();
        InOrder inOrderOut = inOrder(spyOut);
        inOrderOut.verify(spyOut).append('#');
        inOrderOut.verify(spyOut).append(' ');
        inOrderOut.verify(spyOut).append('a');
        inOrderOut.verify(spyOut).append('b');
        inOrderOut.verify(spyOut).append('c');

        // Verify println() called at end (total 2 times)
        verify(spyPrinter, times(2)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_HandlesCRLF() throws IOException {
        String comment = "line1\r\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','1'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF triggers println and append comment start and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','2'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // Final println call appends newline (unknown char)
        verify(out, atLeast(1)).append(anyChar());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_HandlesLF() throws IOException {
        String comment = "line1\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','1'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // LF triggers println and append comment start and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','2'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // Final println call appends newline (unknown char)
        verify(out, atLeast(1)).append(anyChar());

        verifyNoMoreInteractions(out);
    }

    private void setNewRecord(CSVPrinter printer, boolean value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.setBoolean(printer, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}