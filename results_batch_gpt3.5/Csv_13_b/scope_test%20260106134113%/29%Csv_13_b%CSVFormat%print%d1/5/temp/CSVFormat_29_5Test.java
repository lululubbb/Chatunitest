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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_29_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_ReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to get the 'out' field from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(printer);
        assertEquals(appendable, outValue);

        // Access private field 'format' in CSVPrinter via reflection
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(printer);

        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    public void testPrint_WithMockAppendable() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable mockAppendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(mockAppendable);
        assertNotNull(printer);

        // Use reflection to get the 'out' field from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(printer);
        assertEquals(mockAppendable, outValue);

        // Access private field 'format' in CSVPrinter via reflection
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(printer);

        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    public void testPrint_ThrowsIOException() {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Test IOException");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("Test IOException");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        assertThrows(IOException.class, () -> csvFormat.print(throwingAppendable));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));

        assertFalse((Boolean) method.invoke(null, 'a'));
        assertFalse((Boolean) method.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) method.invoke(null, (Character) null));

        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf(',')));
    }
}