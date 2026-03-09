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

class CSVPrinter_9_2Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintln_AppendsRecordSeparatorAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // set newRecord to false to verify it becomes true after println
        setNewRecord(printer, false);

        printer.println();

        verify(formatMock).getRecordSeparator();
        verify(outMock).append(recordSeparator);

        assertTrue(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_ThrowsIOException() throws IOException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(outMock).append(recordSeparator);

        IOException exception = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", exception.getMessage());
    }

    // Helper methods to access private field newRecord via reflection
    private void setNewRecord(CSVPrinter printer, boolean value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getNewRecordField(printer);
        field.setAccessible(true);
        field.setBoolean(printer, value);
    }

    private boolean getNewRecord(CSVPrinter printer) throws NoSuchFieldException, IllegalAccessException {
        Field field = getNewRecordField(printer);
        field.setAccessible(true);
        return field.getBoolean(printer);
    }

    private Field getNewRecordField(CSVPrinter printer) throws NoSuchFieldException {
        return CSVPrinter.class.getDeclaredField("newRecord");
    }
}