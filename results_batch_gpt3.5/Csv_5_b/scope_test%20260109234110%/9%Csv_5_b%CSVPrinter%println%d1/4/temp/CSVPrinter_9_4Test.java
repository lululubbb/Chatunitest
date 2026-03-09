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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_9_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintln_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        // Initially set newRecord to false to verify it becomes true
        setPrivateField(printer, "newRecord", false);

        printer.println();

        verify(out).append(recordSeparator);
        assertTrue(getPrivateBooleanField(printer, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrintln_throwsIOException() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(out).append(recordSeparator);

        IOException thrown = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", thrown.getMessage());
    }

    // Helper methods to access private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = getField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getPrivateBooleanField(Object target, String fieldName) {
        try {
            java.lang.reflect.Field field = getField(target.getClass(), fieldName);
            field.setAccessible(true);
            return field.getBoolean(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to get field from class or superclass
    private java.lang.reflect.Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}