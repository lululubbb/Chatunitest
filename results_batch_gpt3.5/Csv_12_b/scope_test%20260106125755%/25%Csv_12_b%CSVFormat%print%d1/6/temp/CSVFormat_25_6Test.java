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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_25_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_ReturnsCSVPrinterInstance() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, getField(printer, "out"));
        assertEquals(csvFormat, getField(printer, "format"));
    }

    @Test
    @Timeout(8000)
    void testPrint_WithNullAppendable_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.print(null);
        });
    }

    @Test
    @Timeout(8000)
    void testPrint_UsingReflection() throws Exception {
        Appendable appendable = mock(Appendable.class);
        Method printMethod = CSVFormat.class.getMethod("print", Appendable.class);
        Object result = printMethod.invoke(csvFormat, appendable);
        assertNotNull(result);
        assertTrue(result instanceof CSVPrinter);
        CSVPrinter printer = (CSVPrinter) result;
        assertEquals(appendable, getField(printer, "out"));
        assertEquals(csvFormat, getField(printer, "format"));
    }

    // Helper method to access private fields of CSVPrinter via reflection
    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
            return null;
        }
    }
}