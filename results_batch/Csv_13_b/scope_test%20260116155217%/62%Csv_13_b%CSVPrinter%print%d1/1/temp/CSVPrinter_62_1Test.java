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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_62_1Test {

    private CSVPrinter csvPrinter;
    private CSVFormat formatMock;
    private Appendable appendableMock;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, formatMock);
    }

    private void invokePrint(Object printerInstance, Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printerInstance, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_quoteCharacterSet() throws Throwable {
        // Setup
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        // newRecord true initially, so no delimiter appended
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(appendableMock, formatMock));

        // invoke print first time
        invokePrint(spyPrinter, "obj", "value123", 0, 5);

        // verify delimiter not appended because newRecord true
        verify(appendableMock, never()).append(',');

        // Since printAndQuote is private, verify its effect by spying on appendableMock calls
        verify(appendableMock, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        // newRecord should be false now, test by invoking print again
        invokePrint(spyPrinter, "obj2", "value456", 1, 3);

        // verify delimiter appended this time
        verify(appendableMock).append(',');

        // verify append called again for quoted substring
        verify(appendableMock, atLeast(2)).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_escapeCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn(';');

        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(appendableMock, formatMock));

        // force newRecord to false by invoking print once
        invokePrint(spyPrinter, "obj", "value", 0, 5);

        // now newRecord is false, next call should append delimiter
        invokePrint(spyPrinter, "obj2", "value2", 1, 3);

        // verify delimiter appended once (only second call)
        verify(appendableMock).append(';');

        // verify append called at least twice (once for escape, once for delimiter)
        verify(appendableMock, atLeast(2)).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testPrint_noQuoteNoEscape() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('|');

        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(appendableMock, formatMock));

        // first call: newRecord true, no delimiter appended
        invokePrint(spyPrinter, null, "abcdef", 1, 3);

        // verify append with substring called with offset=1, offset+len=4
        verify(appendableMock).append("abcdef", 1, 4);

        // second call: newRecord false, delimiter appended
        invokePrint(spyPrinter, null, "ghijkl", 0, 2);
        verify(appendableMock).append('|');
        verify(appendableMock).append("ghijkl", 0, 2);
    }

    @Test
    @Timeout(8000)
    void testPrint_appendThrowsIOException() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        doThrow(new IOException("append failed")).when(appendableMock).append(any(CharSequence.class), anyInt(), anyInt());

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                printMethod.invoke(csvPrinter, null, "value", 0, 5);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                } else {
                    throw e;
                }
            }
        }, "Expected IOException to be thrown");

        assertEquals("append failed", thrown.getMessage());
    }
}