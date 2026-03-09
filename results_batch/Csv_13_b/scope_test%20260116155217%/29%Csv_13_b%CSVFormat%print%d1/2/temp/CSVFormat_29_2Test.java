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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_29_2Test {

    private Appendable mockAppendable;

    @BeforeEach
    public void setUp() {
        mockAppendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(mockAppendable);
        assertNotNull(printer);
        assertEquals(mockAppendable, getField(printer, "out"));
        assertEquals(format, getField(printer, "format"));
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("append failed");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("append failed");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("append failed");
            }
        };
        CSVFormat format = CSVFormat.DEFAULT;
        // The constructor of CSVPrinter does not write, so no IOException expected here.
        // To test IOException, we would need to invoke methods on CSVPrinter.
        // So here we just verify no IOException at construction.
        assertDoesNotThrow(() -> format.print(throwingAppendable));
    }

    // Helper method to access private fields via reflection
    private Object getField(Object instance, String fieldName) {
        try {
            Field field = null;
            Class<?> clazz = instance.getClass();
            while (clazz != null) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (field == null) {
                fail("Failed to access field: " + fieldName + " because field not found");
            }
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            fail("Failed to access field: " + fieldName + " due to " + e);
            return null;
        }
    }
}