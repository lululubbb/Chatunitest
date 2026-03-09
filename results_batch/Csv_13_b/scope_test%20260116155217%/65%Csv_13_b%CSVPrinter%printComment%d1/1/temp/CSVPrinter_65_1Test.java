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
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NoCommentMarkerSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentMarkerSet();
        verifyNoMoreInteractions(format);
        verifyNoInteractions(format, out);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NewRecordTrue_SingleLineComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(true);

        String comment = "This is a comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_NewRecordFalse_SingleLineComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(false);

        String comment = "Comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        // println() should be called first because newRecord == false
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_MultilineCommentWithCRLF() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(true);

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
        // CRLF triggers println and prefix again
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_MultilineCommentWithLFOnly() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(true);

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
        // LF triggers println and prefix again
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_EmptyComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');
        setNewRecord(true);

        String comment = "";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('\n');
    }

    // Helper method to set private boolean newRecord field via reflection
    private void setNewRecord(boolean value) throws Exception {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, value);
    }
}