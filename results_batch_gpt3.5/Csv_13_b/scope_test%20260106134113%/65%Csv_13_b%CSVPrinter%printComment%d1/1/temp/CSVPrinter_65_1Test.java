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

public class CSVPrinter_65_1Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

    @BeforeEach
    public void setup() throws Exception {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        printer = new CSVPrinter(out, format);

        // Set newRecord field true by default
        setNewRecord(printer, true);
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws NoSuchFieldException, IllegalAccessException {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, value);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_commentMarkerNotSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);
        printer.printComment("ignored");
        verify(out, never()).append(anyChar());
        verify(out, never()).append(anyString());
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordTrue_simpleComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        // newRecord = true, so println() not called before
        setNewRecord(printer, true);

        printer.printComment("hello");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('h');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('o');
        inOrder.verify(out).append(anyChar()); // println() appends line separator, cannot verify exact char here
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordFalse_callsPrintlnBefore() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, false);

        // Spy on printer to verify println() call
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printComment("test");

        verify(spyPrinter).println();

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('t');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('s');
        inOrder.verify(out).append('t');
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_withCRLF() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        // newRecord = true
        setNewRecord(printer, true);

        // comment with CRLF inside: "abc\r\nxyz"
        String comment = "abc\r\nxyz";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        // CRLF triggers println and reprint comment marker and space
        inOrder.verify(out).append(anyChar()); // println() append
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('x');
        inOrder.verify(out).append('y');
        inOrder.verify(out).append('z');
        inOrder.verify(out).append(anyChar()); // final println()
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_withCROnly() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

        // comment with CR only: "abc\rcde"
        String comment = "abc\rcde";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        // CR triggers println and reprint comment marker and space
        inOrder.verify(out).append(anyChar()); // println() append
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('c');
        inOrder.verify(out).append('d');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append(anyChar()); // final println()
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_withLFOnly() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

        // comment with LF only: "abc\ndef"
        String comment = "abc\ndef";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        // LF triggers println and reprint comment marker and space
        inOrder.verify(out).append(anyChar()); // println() append
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('d');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('f');
        inOrder.verify(out).append(anyChar()); // final println()
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_emptyComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

        printer.printComment("");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append(anyChar()); // println()
    }
}