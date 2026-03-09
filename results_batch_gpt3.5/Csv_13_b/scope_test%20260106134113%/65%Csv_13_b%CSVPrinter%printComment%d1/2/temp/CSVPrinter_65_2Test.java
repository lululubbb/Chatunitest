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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_65_2Test {

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
        // Set newRecord to true initially by reflection
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NoCommentMarkerSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);
        // Should return immediately, no interaction with out
        printer.printComment("ignored comment");
        verify(out, never()).append(anyChar());
        verify(out, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SimpleComment() throws IOException {
        // newRecord = true, simple comment without CR/LF
        String comment = "simple comment";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // Should append comment marker and space once
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Should call println() at the end
        inOrder.verify(out).append('\n'); // println() appends newline, see below for println()
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_CallsPrintlnBefore() throws IOException {
        try {
            // Set newRecord to false to test println() call before printing comment
            Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
            newRecordField.setAccessible(true);
            newRecordField.set(printer, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String comment = "test";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // First println call (because newRecord is false)
        inOrder.verify(out).append('\n');
        // Then comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Final println call
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentWithCRLF() throws IOException {
        // comment with CR LF sequence in middle
        String comment = "line1\r\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // initial comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CR detected, next char is LF, so skip LF and println
        inOrder.verify(out).append('\n');
        // after println append comment marker and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println call
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentWithLFOnly() throws IOException {
        // comment with LF only
        String comment = "line1\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // initial comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // LF detected, println and append comment marker and space
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // final println call
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_EmptyComment() throws IOException {
        // empty comment string
        String comment = "";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // initial comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // no chars appended
        // final println call
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_ThrowsIOExceptionFromAppend() throws IOException {
        when(out.append(anyChar())).thenThrow(new IOException("append failed"));
        IOException thrown = assertThrows(IOException.class, () -> printer.printComment("fail"));
        assertEquals("append failed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintlnPrivateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Use reflection to invoke private println method
        Method printlnMethod = CSVPrinter.class.getDeclaredMethod("println");
        printlnMethod.setAccessible(true);

        // We mock Appendable to verify append call for println
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        printer = new CSVPrinter(out, format);

        // Set newRecord to true to avoid println calling twice or interfering
        try {
            Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
            newRecordField.setAccessible(true);
            newRecordField.set(printer, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        printlnMethod.invoke(printer);

        verify(out).append('\n');
    }
}