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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_9_5Test {

    private CSVPrinter printer;
    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrintln_recordSeparatorNotNull_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // Set newRecord to false initially via reflection to test it becomes true
        setNewRecordField(printer, false);

        printer.println();

        verify(outMock).append(recordSeparator);

        assertTrue(getNewRecordField(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_recordSeparatorNull_doesNotAppendButSetsNewRecordTrue() throws IOException {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        // Set newRecord to false initially via reflection to test it becomes true
        setNewRecordField(printer, false);

        printer.println();

        verify(outMock, never()).append(any(CharSequence.class));

        assertTrue(getNewRecordField(printer));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintlnViaReflection_recordSeparatorNotNull() throws Exception {
        String recordSeparator = "\r\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        // Set newRecord to false initially
        setNewRecordField(printer, false);

        Method printlnMethod = CSVPrinter.class.getDeclaredMethod("println");
        printlnMethod.setAccessible(true);
        printlnMethod.invoke(printer);

        verify(outMock).append(recordSeparator);
        assertTrue(getNewRecordField(printer));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintlnViaReflection_recordSeparatorNull() throws Exception {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        setNewRecordField(printer, false);

        Method printlnMethod = CSVPrinter.class.getDeclaredMethod("println");
        printlnMethod.setAccessible(true);
        printlnMethod.invoke(printer);

        verify(outMock, never()).append(any(CharSequence.class));
        assertTrue(getNewRecordField(printer));
    }

    private boolean getNewRecordField(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            return field.getBoolean(printer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setNewRecordField(CSVPrinter printer, boolean value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.setBoolean(printer, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}