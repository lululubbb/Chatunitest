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

class CSVPrinter_65_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        // By default, comment marker is set and returns '#'
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        printer = spy(new CSVPrinter(out, format));

        // Mock println() method behavior to append CR and LF
        doAnswer(invocation -> {
            out.append('\r');
            out.append('\n');
            // set newRecord to true after println
            setNewRecordField(printer, true);
            return null;
        }).when(printer).println();

        // Initialize newRecord to true
        setNewRecordField(printer, true);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NoCommentMarkerSet() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue() throws Exception {
        // newRecord is true by default, so println() should not be called before output
        String comment = "comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // First append comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // Then append each character of comment
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Finally println() called once after loop
        // println() appends record separator (CRLF by default)
        // So verify out.append called with '\r' and '\n' at end
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse() throws Exception {
        setNewRecordField(printer, false);

        String comment = "comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // Because newRecord is false, println() is called before printing comment marker and space
        // println() appends CRLF
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        // Then append comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // Then append each character of comment
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        // Then println() at end
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_WithCRLFInsideComment() throws Exception {
        String comment = "line1\r\nline2\nline3\rline4";
        // comment marker '#'
        // newRecord true by default

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // Initial comment marker and space
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF: skips LF, calls println(), then appends comment marker and space
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // LF: call println(), append comment marker and space
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line3 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('3');

        // CR: call println(), append comment marker and space
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line4 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('4');

        // final println()
        inOrder.verify(out).append('\r');
        inOrder.verify(out).append('\n');

        inOrder.verifyNoMoreInteractions();
    }

    private void setNewRecordField(CSVPrinter printer, boolean value) throws Exception {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.setBoolean(printer, value);
    }
}