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

public class CSVPrinter_65_4Test {

    private CSVFormat formatMock;
    private Appendable outMock;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        outMock = mock(Appendable.class);
        printer = new CSVPrinter(outMock, formatMock);

        // Set newRecord to true initially by reflection (default true)
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, true);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_noCommentMarkerSet() throws IOException {
        when(formatMock.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(formatMock).isCommentMarkerSet();
        verify(formatMock, times(1)).isCommentMarkerSet();
        verifyNoMoreInteractions(formatMock);
        verifyNoInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordTrue_singleLineComment() throws IOException {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('#');

        String comment = "comment";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('#');
        inOrder.verify(outMock).append(' ');

        for (char c : comment.toCharArray()) {
            inOrder.verify(outMock).append(c);
        }

        inOrder.verify(outMock).append('\n');

        verify(formatMock, times(2)).isCommentMarkerSet();
        verify(formatMock, times(1)).getCommentMarker();
        verifyNoMoreInteractions(formatMock);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_newRecordFalse_singleLineComment() throws Exception {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('!');

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        String comment = "test";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('\n');

        inOrder.verify(outMock).append('!');
        inOrder.verify(outMock).append(' ');

        for (char c : comment.toCharArray()) {
            inOrder.verify(outMock).append(c);
        }

        inOrder.verify(outMock).append('\n');

        verify(formatMock, times(2)).isCommentMarkerSet();
        verify(formatMock, times(1)).getCommentMarker();
        verifyNoMoreInteractions(formatMock);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_multilineCommentWithCRLF() throws IOException {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('%');

        String comment = "line1\r\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('%');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('1');

        inOrder.verify(outMock).append('\n');

        inOrder.verify(outMock).append('%');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('2');

        inOrder.verify(outMock).append('\n');

        verify(formatMock, times(2)).isCommentMarkerSet();
        verify(formatMock, times(1)).getCommentMarker();
        verifyNoMoreInteractions(formatMock);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintComment_multilineCommentWithLF() throws IOException {
        when(formatMock.isCommentMarkerSet()).thenReturn(true);
        when(formatMock.getCommentMarker()).thenReturn('*');

        String comment = "line1\nline2";

        printer.printComment(comment);

        InOrder inOrder = inOrder(outMock);

        inOrder.verify(outMock).append('*');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('1');

        inOrder.verify(outMock).append('\n');

        inOrder.verify(outMock).append('*');
        inOrder.verify(outMock).append(' ');

        inOrder.verify(outMock).append('l');
        inOrder.verify(outMock).append('i');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append('e');
        inOrder.verify(outMock).append('2');

        inOrder.verify(outMock).append('\n');

        verify(formatMock, times(2)).isCommentMarkerSet();
        verify(formatMock, times(1)).getCommentMarker();
        verifyNoMoreInteractions(formatMock);
        verify(outMock, never()).append(anyChar()); // replaced verifyNoInteractions(outMock)
    }
}