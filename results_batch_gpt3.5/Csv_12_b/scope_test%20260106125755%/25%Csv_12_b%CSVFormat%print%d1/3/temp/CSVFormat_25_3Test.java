package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_25_3Test {

    private Appendable mockAppendable;

    @BeforeEach
    void setUp() {
        mockAppendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(mockAppendable);
        assertNotNull(printer);
        assertEquals(mockAppendable, getField(printer, "out"));
        assertEquals(format, getField(printer, "format"));
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = mock(Appendable.class);
        // Mock all append methods to throw IOException
        doThrow(new IOException("test exception")).when(throwingAppendable).append(any(CharSequence.class));
        doThrow(new IOException("test exception")).when(throwingAppendable).append(any(CharSequence.class), anyInt(), anyInt());
        doThrow(new IOException("test exception")).when(throwingAppendable).append(anyChar());

        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(throwingAppendable);

        IOException thrown = assertThrows(IOException.class, () -> {
            printer.print("test");
        });
        assertEquals("test exception", thrown.getMessage());
    }

    // Utility method to access private fields via reflection, including in superclasses
    private Object getField(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                fail("Failed to get field " + fieldName + ": " + e);
                return null;
            }
        }
        fail("Field " + fieldName + " not found in class hierarchy");
        return null;
    }
}