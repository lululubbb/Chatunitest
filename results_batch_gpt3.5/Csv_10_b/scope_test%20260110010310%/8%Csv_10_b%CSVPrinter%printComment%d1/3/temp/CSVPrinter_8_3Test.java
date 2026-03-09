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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_8_3Test {

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
    void testPrintComment_CommentingDisabled() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(false);

        printer.printComment("any comment");

        verify(format).isCommentingEnabled();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordTrue_NoLineBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        printer.printComment("abc");

        verify(out).append('#');
        verify(out).append(' ');
        verify(out).append('a');
        verify(out).append('b');
        verify(out).append('c');
        verify(out, times(4)).append('\n'); // println() called 4 times in total (3 in loop + 1 final)
    }

    @Test
    @Timeout(8000)
    void testPrintComment_NewRecordFalse_LineBefore() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        setField(printer, "newRecord", false);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).println();

        spyPrinter.printComment("abc");

        verify(spyPrinter).println(); // called once before comment start
        verify(out).append('#');
        verify(out).append(' ');
        verify(out).append('a');
        verify(out).append('b');
        verify(out).append('c');
        verify(spyPrinter, times(4)).println(); // 1 before + 3 in loop + final
    }

    @Test
    @Timeout(8000)
    void testPrintComment_WithCRAndLF() throws IOException {
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        String comment = "a\r\nb\nc\r\n";

        printer.printComment(comment);

        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);

        verify(out, atLeastOnce()).append(captor.capture());

        long commentStartCount = captor.getAllValues().stream().filter(c -> c == '#').count();
        long spaceCount = captor.getAllValues().stream().filter(c -> c == ' ').count();

        assertTrue(commentStartCount >= 4);
        assertTrue(spaceCount >= 4);

        verify(out).append('a');
        verify(out).append('b');
        verify(out).append('c');

        verify(out, atLeast(5)).append('\n'); // println() called 5 times in total for this comment
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}