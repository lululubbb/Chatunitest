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
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_8_6Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        // Should return immediately without any output
        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_NoLineBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // newRecord is true initially, so no println() before comment
        printer.printComment("abc");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        inOrder.verify(out).append('\n');
        verify(format).isCommentingEnabled();
        verify(format, times(2)).getCommentStart();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_LineBefore() throws IOException, Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // Set newRecord = false by reflection
        java.lang.reflect.Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Spy on printer to verify println() calls
        CSVPrinter spyPrinter = spy(printer);

        // We want to call the real method, but verify println() calls
        doCallRealMethod().when(spyPrinter).printComment(anyString());
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("abc");

        InOrder inOrder = inOrder(spyPrinter, out);
        // First println because newRecord is false
        inOrder.verify(spyPrinter).println();
        // Then append comment start and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // Then append characters
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        // Then println at end
        inOrder.verify(spyPrinter).println();

        verify(format).isCommentingEnabled();
        verify(format, times(2)).getCommentStart();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_MultilineWithCRLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // comment with CRLF
        String comment = "line1\r\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // First line start
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','1'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF triggers println + comment start + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','2'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println
        inOrder.verify(out).append('\n');

        verify(format).isCommentingEnabled();
        verify(format, times(3)).getCommentStart();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_MultilineWithLFOnly() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // comment with LF only
        String comment = "line1\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // First line start
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','1'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // LF triggers println + comment start + space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // 'l','i','n','e','2'
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println
        inOrder.verify(out).append('\n');

        verify(format).isCommentingEnabled();
        verify(format, times(3)).getCommentStart();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_EmptyComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer.printComment("");

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('\n');

        verify(format).isCommentingEnabled();
        verify(format, times(2)).getCommentStart();
    }
}