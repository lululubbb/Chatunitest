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

class CSVFormat_25_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);
        assertEquals(appendable, outValue);

        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(printer);
        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullAppendableThrowsException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print(null));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakChar() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    void testPrivateIsLineBreakCharacter() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, (Object) null));
    }
}