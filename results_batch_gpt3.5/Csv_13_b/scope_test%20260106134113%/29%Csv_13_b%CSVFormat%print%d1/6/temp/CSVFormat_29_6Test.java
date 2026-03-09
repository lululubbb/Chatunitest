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

public class CSVFormat_29_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        // Use default CSVFormat instance for tests
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithValidAppendable_ShouldReturnCSVPrinter() throws IOException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertSame(appendable, getAppendableFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithMockAppendable_ShouldReturnCSVPrinter() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertSame(appendable, getAppendableFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithNullAppendable_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print(null));
    }

    // Reflection helper to get the private field 'out' from CSVPrinter instance
    private Appendable getAppendableFromPrinter(CSVPrinter printer) {
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            return (Appendable) outField.get(printer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to access 'out' field in CSVPrinter: " + e.getMessage());
            return null;
        }
    }
}