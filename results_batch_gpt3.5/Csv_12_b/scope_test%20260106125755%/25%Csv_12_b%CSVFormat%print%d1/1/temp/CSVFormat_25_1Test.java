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

public class CSVFormat_25_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrintCreatesCSVPrinter() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, getAppendableFromPrinter(printer));
        assertEquals(csvFormat, getCSVFormatFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullAppendableThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.print(null);
        });
    }

    // Helper methods to access private fields of CSVPrinter via reflection
    private Appendable getAppendableFromPrinter(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("out");
            field.setAccessible(true);
            return (Appendable) field.get(printer);
        } catch (Exception e) {
            fail("Reflection failed to get 'out' field from CSVPrinter: " + e.getMessage());
            return null;
        }
    }

    private CSVFormat getCSVFormatFromPrinter(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("format");
            field.setAccessible(true);
            return (CSVFormat) field.get(printer);
        } catch (Exception e) {
            fail("Reflection failed to get 'format' field from CSVPrinter: " + e.getMessage());
            return null;
        }
    }
}