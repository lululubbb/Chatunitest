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

public class CSVPrinter_65_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_noCommentMarkerSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(format, times(1)).isCommentMarkerSet();
        verifyNoMoreInteractions(format);
        verifyNoMoreInteractions(out); // fixed: verifyNoInteractions(format) -> verifyNoMoreInteractions(out)
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordTrue_simpleComment() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        // newRecord is true by default, no println() call expected before printing comment
        setNewRecord(printer, true);

        String comment = "simple comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }
        inOrder.verify(out).append('\n'); // println() appends newline char, see println() below

        // verify println called once at the end
        // We check println invocation by reflection to count its calls
        // but simpler to check append('\n') calls in order.

        verify(format, times(2)).getCommentMarker();
        verify(format, times(1)).isCommentMarkerSet();
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordFalse_invokesPrintlnBefore() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, false);

        String comment = "comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        // Because newRecord is false, println() is called first
        // println() appends '\n'
        inOrder.verify(out).append('\n');

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        for (char c : comment.toCharArray()) {
            inOrder.verify(out).append(c);
        }

        inOrder.verify(out).append('\n'); // println() at the end

        verify(format, times(2)).getCommentMarker();
        verify(format, times(1)).isCommentMarkerSet();
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_commentWithCRLF() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

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

        // CRLF detected: println(), then append comment marker + SP again
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // line2 chars
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');

        inOrder.verify(out).append('\n'); // final println()

        verify(format, times(3)).getCommentMarker();
        verify(format, times(1)).isCommentMarkerSet();
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_commentWithCRAndLFSeparately() throws Exception {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

        String comment = "start\rmiddle\nend";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);

        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // start
        inOrder.verify(out).append('s');
        inOrder.verify(out).append('t');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append('t');

        // CR: println() then append comment marker + SP
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // middle
        inOrder.verify(out).append('m');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('d');
        inOrder.verify(out).append('d');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('e');

        // LF: println() then append comment marker + SP
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');

        // end
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('d');

        inOrder.verify(out).append('\n'); // final println()

        verify(format, times(4)).getCommentMarker();
        verify(format, times(1)).isCommentMarkerSet();
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, value);
    }
}