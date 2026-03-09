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

class CSVPrinter_66_1Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithRecordSeparator_AppendsSeparatorAndSetsNewRecordTrue() throws Exception {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // Initially set newRecord to false to verify it changes to true
        setNewRecordField(printer, false);

        printer.println();

        verify(outMock).append(recordSeparator);
        assertTrue(getNewRecordField(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithNullRecordSeparator_DoesNotAppendAndSetsNewRecordTrue() throws Exception {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        // Initially set newRecord to false to verify it changes to true
        setNewRecordField(printer, false);

        printer.println();

        verify(outMock, never()).append(anyString());
        assertTrue(getNewRecordField(printer));
    }

    private void setNewRecordField(CSVPrinter printer, boolean value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.setBoolean(printer, value);
    }

    private boolean getNewRecordField(CSVPrinter printer) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return field.getBoolean(printer);
    }
}