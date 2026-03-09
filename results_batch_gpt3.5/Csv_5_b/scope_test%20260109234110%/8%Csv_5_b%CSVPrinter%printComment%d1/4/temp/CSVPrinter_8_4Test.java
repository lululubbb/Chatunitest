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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_8_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        // Should return immediately, no output
        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordTrue_singleLineComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // newRecord is true by default, so println() is not called before output

        String comment = "This is a comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // Should append comment start char and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // Each character appended
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }

        // println() called at end: which appends newline(s)
        // println() method is public, but we do not know its implementation exactly.
        // It likely appends CR and/or LF to out. We verify out.append called at least once for println.
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordFalse_invokesPrintlnBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        // set newRecord to false via reflection
        setField(printer, "newRecord", false);

        String comment = "comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // Because newRecord is false, println() called before appending comment start and space
        // println() appends at least one char
        inOrder.verify(out, atLeastOnce()).append(anyChar());

        // Then comment start and space appended
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // Then comment chars appended
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }

        // Finally println() called again at end
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_multilineCommentWithCRLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        String comment = "line1\r\nline2\nline3\rline4";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // Initial comment start and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        for (char c : "line1".toCharArray()) {
            inOrder.verify(out).append(c);
        }

        // Verify comment start and space appended 5 times total (initial + 4 after println)
        verify(out, times(5)).append('#'); // initial + 4 after println
        verify(out, times(5)).append(' ');

        // Each println calls out.append at least once, so out.append called at least 5 times for newline chars
        verify(out, atLeast(5)).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_emptyComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        String comment = "";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // comment start and space appended
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // No chars appended because comment is empty

        // println called at end
        verify(out, atLeastOnce()).append(anyChar());
    }

    // Utility method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection error setting field " + fieldName + ": " + e.getMessage());
        }
    }
}