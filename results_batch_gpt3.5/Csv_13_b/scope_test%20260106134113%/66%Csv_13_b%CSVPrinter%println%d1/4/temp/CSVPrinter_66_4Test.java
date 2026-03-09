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

public class CSVPrinter_66_4Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithRecordSeparator_AppendsRecordSeparatorAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // Initially set newRecord to false to verify it changes to true
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.setBoolean(printer, false);

        printer.println();

        verify(outMock).append(recordSeparator);
        assertTrue(newRecordField.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithNullRecordSeparator_DoesNotAppendAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        // Initially set newRecord to false to verify it changes to true
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.setBoolean(printer, false);

        printer.println();

        verify(outMock, never()).append(any(CharSequence.class));
        assertTrue(newRecordField.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintln_AppendThrowsIOException_PropagatesException() throws IOException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(outMock).append(recordSeparator);

        IOException thrown = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", thrown.getMessage());
    }
}