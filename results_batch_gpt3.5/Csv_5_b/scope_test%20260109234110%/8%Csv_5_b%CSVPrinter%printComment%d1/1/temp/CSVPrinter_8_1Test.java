package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_8_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);
        CSVPrinter printerDisabled = new CSVPrinter(out, format);

        printerDisabled.printComment("any comment");

        verify(out, never()).append(anyChar());
        verify(out, never()).append(anyString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_NoNewLineBefore() throws Exception {
        // newRecord is true by default, no println() before comment

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printComment("test comment");

        // Verify out.append called with comment start and SP at least once
        verify(out).append('#');
        verify(out).append(SP);

        // Verify that characters are appended properly
        verify(out, atLeastOnce()).append(anyChar());

        // Verify println() is called at end - println() calls out.append('\n') or similar
        verify(spyPrinter, atLeastOnce()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_NewLineBefore() throws Exception {
        // Set private field newRecord to false
        setNewRecord(printer, false);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        setNewRecord(spyPrinter, false);
        spyPrinter.printComment("comment");

        // When newRecord is false, println() should be called before appending comment start
        verify(spyPrinter, atLeastOnce()).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_WithCRLF() throws Exception {
        String comment = "line1\r\nline2\nline3\rline4";

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printComment(comment);

        // Verify out.append called with comment start and SP multiple times (for each line)
        verify(out, atLeast(4)).append('#');
        verify(out, atLeast(4)).append(SP);

        // Verify println() called multiple times for line breaks
        verify(spyPrinter, atLeast(4)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintComment_EmptyComment() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printComment("");

        verify(out).append('#');
        verify(out).append(SP);
        // No characters appended except comment start and SP, so no anyChar() calls expected here
        verify(spyPrinter, atLeastOnce()).println();
    }

    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, value);
    }
}