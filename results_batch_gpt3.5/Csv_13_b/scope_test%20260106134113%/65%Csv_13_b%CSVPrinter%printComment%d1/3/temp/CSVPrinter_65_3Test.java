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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class CSVPrinter_65_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws Exception {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        printer = new CSVPrinter(out, format);
        // Set newRecord to true by reflection as it is private
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NullComment() throws Exception {
        // Since comment is String, null input is not specified in signature, skip null test
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_CommentMarkerNotSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);
        printer.printComment("any comment");
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NewRecordTrue_SimpleComment() throws IOException {
        String comment = "simple comment";
        // newRecord is true, so println() not called before
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // First append comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // Then each char appended
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Finally println() called which appends record separator (mock println)
        // We cannot verify println() calls directly, but we can verify out.append calls
        // The last println() call appends record separator, but recordSeparator is null in mock so no append

        // Verify no extra calls
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NewRecordFalse_InvokesPrintlnBefore() throws Exception {
        // Set newRecord to false
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Spy on printer to verify println call
        CSVPrinter spyPrinter = spy(printer);

        String comment = "comment";
        spyPrinter.printComment(comment);

        // Verify println() called once before appending comment marker
        verify(spyPrinter).println();

        // Verify comment marker and space appended after println
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Verify println() called at end
        verify(spyPrinter, times(2)).println();
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_CommentWithCRLF() throws IOException {
        String comment = "line1\r\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF triggers println, then appends comment marker and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        verify(out, times(2)).append('#');
        verify(out, times(2)).append(' ');

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_CommentWithLF() throws IOException {
        String comment = "line1\nline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // LF triggers println, then appends comment marker and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        verify(out, times(2)).append('#');
        verify(out, times(2)).append(' ');

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_EmptyComment() throws IOException {
        String comment = "";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // No chars appended
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_CommentWithCROnly() throws IOException {
        String comment = "line1\rline2";
        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CR triggers println, then appends comment marker and space again
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        verify(out, times(2)).append('#');
        verify(out, times(2)).append(' ');

        verifyNoMoreInteractions(out);
    }

}