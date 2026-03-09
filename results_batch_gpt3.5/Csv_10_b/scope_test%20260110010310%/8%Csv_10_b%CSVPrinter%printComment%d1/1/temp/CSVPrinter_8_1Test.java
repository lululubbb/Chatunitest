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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_8_1Test {

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
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        // Should return immediately, no output
        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SingleLineComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // newRecord is true by default, so println() is not called before output
        printer.printComment("hello");

        InOrder inOrder = inOrder(out);
        // append comment start char
        inOrder.verify(out).append('#');
        // append space
        inOrder.verify(out).append(' ');
        // append each character
        inOrder.verify(out).append('h');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('o');
        // println() called at end appends newline
        inOrder.verify(out).append('\n');

        verify(out, times(8)).append(anyChar());
        verify(format, times(2)).getCommentStart();
        verify(format).isCommentingEnabled();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_MultiLineCommentWithCRLF() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // Set newRecord to false using reflection to test println() call at start
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Spy on printer to verify println() calls
        CSVPrinter spyPrinter = spy(printer);

        // Call printComment on spy to verify println calls
        spyPrinter.printComment("line1\r\nline2\nline3\rline4");

        InOrder inOrder = inOrder(spyPrinter, out);

        // Because newRecord is false, println() is called before output
        inOrder.verify(spyPrinter).println();

        // Then append comment start and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // Walk through characters:
        // line1\r\nline2\nline3\rline4
        // For CRLF at line1\r\n, i increments by 2, prints newline, and prints comment start + space again
        // For LF at line2\n, prints newline and comment start + space again
        // For CR at line3\r, prints newline and comment start + space again

        // Verify append calls for characters other than CR and LF
        verify(out, atLeast(10)).append(anyChar());

        // Verify println() called appropriate number of times:
        // Once before output (newRecord false)
        // Once for CRLF
        // Once for LF
        // Once for CR
        // Once at end
        verify(spyPrinter, times(5)).println();

        verify(format, atLeast(5)).getCommentStart();
        verify(format).isCommentingEnabled();
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
        // no characters appended because comment is empty
        inOrder.verify(out).append('\n'); // println() appends newline
        verify(format).isCommentingEnabled();
        verify(format, times(2)).getCommentStart();
    }
}