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

class CSVPrinter_9_1Test {

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
    void println_RecordSeparatorNotNull_AppendsSeparatorAndSetsNewRecordTrue() throws Exception {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // Initially set newRecord to false to verify it becomes true after println
        setNewRecord(printer, false);

        printer.println();

        verify(outMock, times(1)).append(recordSeparator);
        assertTrue(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void println_RecordSeparatorNull_DoesNotAppendButSetsNewRecordTrue() throws Exception {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        // Initially set newRecord to false to verify it becomes true after println
        setNewRecord(printer, false);

        printer.println();

        verify(outMock, never()).append(any(CharSequence.class));
        assertTrue(getNewRecord(printer));
    }

    // Helper methods to access private boolean newRecord field via reflection
    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.setBoolean(printer, value);
    }

    private boolean getNewRecord(CSVPrinter printer) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return field.getBoolean(printer);
    }
}