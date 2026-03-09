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

class CSVFormat_29_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_ReturnsCSVPrinterInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable mockAppendable = mock(Appendable.class);

        CSVPrinter printer = csvFormat.print(mockAppendable);

        assertNotNull(printer);

        // CSVPrinter.out is declared in superclass AbstractCSVPrinter, so getDeclaredField on printer.getClass() may fail.
        // Instead, get the field from AbstractCSVPrinter.class or its superclass.
        Field outField = getFieldFromClassHierarchy(printer.getClass(), "out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);
        assertEquals(mockAppendable, outValue);

        Field formatField = getFieldFromClassHierarchy(printer.getClass(), "format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(printer);
        assertEquals(csvFormat, formatValue);
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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