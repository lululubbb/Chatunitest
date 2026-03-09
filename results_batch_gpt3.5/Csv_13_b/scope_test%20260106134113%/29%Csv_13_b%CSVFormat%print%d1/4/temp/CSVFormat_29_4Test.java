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
    void testPrint_WithValidAppendable_ReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to access the private 'out' field of CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(printer);

        assertSame(appendable, outValue);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithAppendableThrowingIOException_PropagatesException() throws IOException {
        Appendable appendable = mock(Appendable.class);
        // Mock append(CharSequence) to throw IOException
        doThrow(new IOException("Test IOException")).when(appendable).append(any(CharSequence.class));
        // Mock append(char) to throw IOException as well, since CSVPrinter may call append(char)
        doThrow(new IOException("Test IOException")).when(appendable).append(anyChar());

        IOException thrown = assertThrows(IOException.class, () -> csvFormat.print(appendable));
        assertEquals("Test IOException", thrown.getMessage());
    }
}