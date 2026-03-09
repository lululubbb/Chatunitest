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

class CSVPrinter_8_2Test {

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
    void printComment_commentingDisabled_noOutput() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        printer.printComment("any comment");

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void printComment_newRecordTrue_simpleComment() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, true);

        printer.printComment("abc");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_newRecordFalse_invokesPrintlnBefore() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, false);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("abc");

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).println();
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('a');
        inOrder.verify(out).append('b');
        inOrder.verify(out).append('c');
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void printComment_commentWithCRLF() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, true);

        String comment = "line1\r\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_commentWithCROnly() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, true);

        String comment = "line1\rline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_commentWithLFOnly() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, true);

        String comment = "line1\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('1');
        inOrder.verify(out).append('\n');
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('l');
        inOrder.verify(out).append('i');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append('e');
        inOrder.verify(out).append('2');
        inOrder.verify(out).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_emptyComment() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setNewRecord(printer, true);

        printer.printComment("");

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('#');
        inOrder.verify(out).append(' ');
        inOrder.verify(out).append('\n');
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.setBoolean(printer, value);
    }
}