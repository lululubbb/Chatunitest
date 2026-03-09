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

class CSVFormat_29_6Test {

    private Appendable appendable;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_ReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Access 'out' field declared in CSVPrinter's superclass or CSVPrinter itself
        Object outField = getField(printer, "out");
        assertSame(appendable, outField);

        Object formatField = getField(printer, "format");
        assertSame(csvFormat, formatField);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithIOException() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("forced");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("forced");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("forced");
            }
        };
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        IOException thrown = assertThrows(IOException.class, () -> csvFormat.print(throwingAppendable));
        assertEquals("forced", thrown.getMessage());
    }

    // Helper method to access private fields via reflection, including superclasses
    private Object getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy");
    }
}