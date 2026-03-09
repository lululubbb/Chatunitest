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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_66_2Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);

        // Use the constructor with Appendable and CSVFormat
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withNonNullRecordSeparator_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        printer.println();

        verify(outMock, times(1)).append(recordSeparator);
        assertTrue(newRecordField.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_withNullRecordSeparator_doesNotAppendButSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        printer.println();

        verify(outMock, never()).append(any(CharSequence.class));
        assertTrue(newRecordField.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_outAppendThrowsIOException_propagatesException() throws IOException {
        String recordSeparator = "\r\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(outMock).append(recordSeparator);

        IOException thrown = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", thrown.getMessage());
    }
}