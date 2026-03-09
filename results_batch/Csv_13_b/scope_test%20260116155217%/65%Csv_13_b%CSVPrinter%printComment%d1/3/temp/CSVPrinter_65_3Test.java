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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_65_3Test {

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
    void testPrintComment_NoCommentMarkerSet() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentMarkerSet();
        verifyNoMoreInteractions(format, out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_SingleLineComment() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, true);

        String comment = "simple comment";

        printer.printComment(comment);

        verify(format).isCommentMarkerSet();
        verify(format).getCommentMarker();
        // verify out.append called with comment marker and space first
        verify(out).append('#');
        verify(out).append(' ');
        // verify each character appended
        for (char c : comment.toCharArray()) {
            verify(out).append(c);
        }
        // verify println called once at end
        verifyPrintlnCalledTimes(printer, 1);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_MultiLineCommentWithCRLF() throws IOException {
        when(format.isCommentMarkerSet()).thenReturn(true);
        when(format.getCommentMarker()).thenReturn('#');

        setNewRecord(printer, false);

        String comment = "line1\r\nline2\nline3\rline4";

        printer.printComment(comment);

        verify(format).isCommentMarkerSet();
        verify(format, atLeastOnce()).getCommentMarker();
        verify(out, atLeast(4)).append('#');
        verify(out, atLeast(4)).append(' ');

        // Verify characters appended excluding CR and LF
        for (char c : comment.toCharArray()) {
            if (c != '\r' && c != '\n') {
                verify(out, atLeastOnce()).append(c);
            }
        }
        // verify println called multiple times (once for newRecord false + 3 line breaks + final)
        verifyPrintlnCalledTimes(printer, 5);
    }

    private void setNewRecord(CSVPrinter printerInstance, boolean value) {
        try {
            Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
            newRecordField.setAccessible(true);
            newRecordField.set(printerInstance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyPrintlnCalledTimes(CSVPrinter printerInstance, int times) throws IOException {
        try {
            // Instead of reflection on println method, directly verify out.append('\n') times
            verify(out, times(times)).append('\n');
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}