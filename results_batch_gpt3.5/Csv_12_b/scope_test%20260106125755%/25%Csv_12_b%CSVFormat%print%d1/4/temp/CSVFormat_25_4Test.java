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

public class CSVFormat_25_4Test {

    private Appendable appendable;

    @BeforeEach
    public void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(appendable);
        assertNotNull(printer);
        // Verify that printer uses the same Appendable and CSVFormat
        assertEquals(appendable, getField(printer, "out"));
        assertEquals(format, getField(printer, "format"));
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = mock(Appendable.class);
        doThrow(new IOException("test exception")).when(throwingAppendable).append(any(CharSequence.class));
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(throwingAppendable);
        assertNotNull(printer);
        // Writing through printer should throw IOException
        IOException thrown = assertThrows(IOException.class, () -> {
            printer.print("test");
        });
        assertEquals("test exception", thrown.getMessage());
    }

    // Helper to access private fields via reflection, including superclass fields
    private Object getField(Object instance, String fieldName) {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(instance);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                fail("Reflection failed: " + e.getMessage());
                return null;
            }
        }
        fail("Field '" + fieldName + "' not found in class hierarchy");
        return null;
    }
}