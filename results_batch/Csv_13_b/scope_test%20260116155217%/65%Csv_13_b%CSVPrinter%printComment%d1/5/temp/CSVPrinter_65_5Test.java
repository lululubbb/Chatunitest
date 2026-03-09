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

public class CSVPrinter_65_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = createCSVPrinter(out, format);
    }

    private CSVPrinter createCSVPrinter(Appendable out, CSVFormat format) {
        try {
            return new CSVPrinter(out, format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_commentMarkerNotSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentMarkerSet();
        verifyNoMoreInteractions(format);
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordTrue_singleLineComment() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setField(printer, "newRecord", true);

        printer.printComment("simple comment");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : "simple comment".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n'); // println() appends recordSeparator, assumed '\n'
        verify(format).isCommentMarkerSet();
        verify(format).getCommentMarker();
        verifyNoMoreInteractions(format);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordFalse_singleLineComment() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setField(printer, "newRecord", false);

        // Spy on printer to verify println() call
        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("comment");

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : "comment".toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(spyPrinter).println();
        verify(format).isCommentMarkerSet();
        verify(format).getCommentMarker();
        verifyNoMoreInteractions(format);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_multilineCommentWithCRLF() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setField(printer, "newRecord", true);

        String comment = "line1\r\nline2\nline3\rline4";

        // Spy to verify println calls
        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment(comment);

        InOrder inOrder = inOrder(out, spyPrinter);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line1 before CRLF
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');

        // CRLF detected, println called once and comment marker appended again
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 before LF
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        // LF detected, println called and comment marker appended again
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line3 before CR
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('3');

        // CR detected, println called and comment marker appended again
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line4
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('4');

        // final println call
        inOrder.verify(spyPrinter).println();

        verify(format).isCommentMarkerSet();
        verify(format, atLeastOnce()).getCommentMarker();
        verifyNoMoreInteractions(format);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_emptyComment() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setField(printer, "newRecord", true);

        printer.printComment("");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('\n'); // println() call appends recordSeparator assumed '\n'
        verify(format).isCommentMarkerSet();
        verify(format).getCommentMarker();
        verifyNoMoreInteractions(format);
    }
}