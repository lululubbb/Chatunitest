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

public class CSVFormat_29_3Test {

    private Appendable appendable;

    @BeforeEach
    public void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to get the private fields from CSVPrinter
        Field outField = getFieldFromClassHierarchy(printer.getClass(), "out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);
        assertSame(appendable, outValue);

        Field formatField = getFieldFromClassHierarchy(printer.getClass(), "format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(printer);
        assertSame(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = mock(Appendable.class);
        doThrow(new IOException("Test IO Exception")).when(throwingAppendable).append(any(CharSequence.class));
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // We expect no IOException on print creation, since print only creates CSVPrinter.
        // IOException may be thrown later during printing, so here no exception expected.
        assertDoesNotThrow(() -> csvFormat.print(throwingAppendable));
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        fail("Field '" + fieldName + "' not found in class hierarchy of " + clazz);
        return null; // unreachable
    }
}