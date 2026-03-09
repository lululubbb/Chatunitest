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

class CSVPrinter_65_5Test {

    private CSVFormat formatMock;
    private Appendable outMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        outMock = mock(Appendable.class);
        printer = new CSVPrinter(outMock, formatMock);

        // Set newRecord field accessible and true by default
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);
    }

    @Test
    @Timeout(8000)
    void printComment_noCommentMarkerSet_doesNothing() throws IOException {
        when(formatMock.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(formatMock).isCommentMarkerSet();
        verifyNoMoreInteractions(formatMock);
        verify(outMock, never()).append(any());
    }

    @Test
    @Timeout(8000)
    void printComment_newRecordTrue_printsCommentProperly() throws Exception {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('#');

        String comment = "simple comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(outMock).append(c);
        }
        inOrder.verify(outMock).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_newRecordFalse_callsPrintlnBeforeComment() throws Exception {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('#');

        // set newRecord to false
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CSVPrinter spyPrinter = spy(printer);

        String comment = "comment";

        spyPrinter.printComment(comment);

        InOrder inOrder = inOrder(spyPrinter, outMock);

        inOrder.verify(spyPrinter).println();
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        for (char c : comment.toCharArray()) {
            inOrder.verify(outMock).append(c);
        }
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void printComment_commentWithCRLFAndLF_printsCorrectly() throws Exception {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('#');

        String comment = "line1\r\nline2\nline3";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('1');

        inOrder.verify(outMock).append('\n');
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('2');

        inOrder.verify(outMock).append('\n');
        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('3');

        inOrder.verify(outMock).append('\n');
    }

    @Test
    @Timeout(8000)
    void printComment_emptyComment_printsOnlyMarkersAndSpaces() throws Exception {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('#');

        String comment = "";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');
        inOrder.verify(outMock).append('\n');
    }
}