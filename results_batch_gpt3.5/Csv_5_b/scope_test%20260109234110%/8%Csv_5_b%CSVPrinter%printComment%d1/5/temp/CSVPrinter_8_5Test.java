package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.SP;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_8_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_commentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);
        printer = new CSVPrinter(out, format);

        printer.printComment("any comment");

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordTrue_singleLineComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        printer.printComment("Hello World");

        String expected = "#" + SP + "Hello World" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_newRecordFalse_callsPrintlnBeforeComment() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", false);

        printer.printComment("Test");

        String expected = System.lineSeparator() + "#" + SP + "Test" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_multilineWithCRLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        // comment with CRLF in middle
        String comment = "Line1\r\nLine2";

        printer.printComment(comment);

        String lineSep = System.lineSeparator();
        String expected = "#" + SP + "Line1" + lineSep + "#" + SP + "Line2" + lineSep;
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_multilineWithCR() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        // comment with CR only (no LF)
        String comment = "Line1\rLine2";

        printer.printComment(comment);

        String lineSep = System.lineSeparator();
        String expected = "#" + SP + "Line1" + lineSep + "#" + SP + "Line2" + lineSep;
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_multilineWithLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        // comment with LF only
        String comment = "Line1\nLine2";

        printer.printComment(comment);

        String lineSep = System.lineSeparator();
        String expected = "#" + SP + "Line1" + lineSep + "#" + SP + "Line2" + lineSep;
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_emptyComment() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        printer.printComment("");

        String expected = "#" + SP + System.lineSeparator();
        assertEquals(expected, out.toString());
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
}