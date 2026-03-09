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

class CSVPrinter_9_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withNonNullRecordSeparator_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        // Initially set newRecord to false to verify it becomes true after println()
        setPrivateBooleanField(printer, "newRecord", false);

        printer.println();

        verify(out).append(recordSeparator);
        assertTrue(getPrivateBooleanField(printer, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrintln_withNullRecordSeparator_doesNotAppendAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(format.getRecordSeparator()).thenReturn(null);

        // Initially set newRecord to false to verify it becomes true after println()
        setPrivateBooleanField(printer, "newRecord", false);

        printer.println();

        verify(out, never()).append(any(CharSequence.class));
        assertTrue(getPrivateBooleanField(printer, "newRecord"));
    }

    // Helper method to set private boolean field newRecord
    private void setPrivateBooleanField(Object instance, String fieldName, boolean value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(instance.getClass(), fieldName);
        field.setAccessible(true);
        field.setBoolean(instance, value);
    }

    // Helper method to get private boolean field newRecord
    private boolean getPrivateBooleanField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(instance.getClass(), fieldName);
        field.setAccessible(true);
        return field.getBoolean(instance);
    }

    // Helper method to get field from class or its superclasses
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}