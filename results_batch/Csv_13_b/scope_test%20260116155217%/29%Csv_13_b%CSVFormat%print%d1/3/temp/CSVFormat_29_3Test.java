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

class CSVFormat_29_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use the default CSVFormat for tests
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer, "CSVPrinter should not be null");

        // Use reflection to get the private 'out' field from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable out = (Appendable) outField.get(printer);
        assertEquals(appendable, out, "Appendable passed should be used in CSVPrinter");

        // Use reflection to get the private 'format' field from CSVPrinter
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat format = (CSVFormat) formatField.get(printer);

        // The CSVPrinter should have a reference to the CSVFormat used
        assertEquals(csvFormat, format, "CSVFormat used should be the one passed");
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOException() throws IOException {
        Appendable appendable = mock(Appendable.class);
        // CSVPrinter constructor does not throw IOException itself,
        // so we simulate IOException by mocking Appendable to throw on append
        doThrow(new IOException("Mock IO Exception")).when(appendable).append(any(CharSequence.class));
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        // Attempt to print something to trigger IOException
        assertThrows(IOException.class, () -> printer.print("test"));
    }
}