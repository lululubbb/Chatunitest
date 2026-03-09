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
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_9_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getRecordSeparator()).thenReturn(null);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void println_recordSeparatorNotNull_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        setNewRecord(printer, false);

        printer.println();

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(recordSeparator);

        assertTrue(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void println_recordSeparatorNull_doesNotAppendButSetsNewRecordTrue() throws IOException {
        when(format.getRecordSeparator()).thenReturn(null);

        setNewRecord(printer, false);

        printer.println();

        verify(out, never()).append(any(CharSequence.class));
        assertTrue(getNewRecord(printer));
    }

    private void setNewRecord(CSVPrinter printer, boolean value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.setBoolean(printer, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getNewRecord(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            return field.getBoolean(printer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}