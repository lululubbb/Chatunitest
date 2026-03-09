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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withValidValues_shouldReturnFormattedString() throws Exception {
        String[] values = new String[] {"a", "b", "c"};

        String result = csvFormat.format((Object[]) values);

        // The default delimiter is comma, default quote char is '"', record separator is CRLF
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues_shouldReturnFormattedStringWithNulls() {
        Object[] values = new Object[] {null, "value", null};

        CSVFormat formatWithNullString = csvFormat.withNullString("NULL");

        String result = formatWithNullString.format(values);

        assertEquals("NULL,\"value\",NULL", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues_shouldReturnEmptyString() {
        Object[] values = new Object[] {};

        String result = csvFormat.format(values);

        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_whenIOExceptionThrown_shouldThrowIllegalStateException() throws Exception {
        // Create a mock CSVPrinter
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Disambiguate printRecord method by specifying varargs signature using any(Object[].class)
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord((Object[]) any());

        // Create a dynamic proxy for CSVFormat that overrides the format method to use the mocked CSVPrinter
        CSVFormat proxyFormat = createCSVFormatProxy(csvFormat, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            proxyFormat.format("a");
        });

        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    private CSVFormat createCSVFormatProxy(CSVFormat delegate, CSVPrinter printer) throws Exception {
        return (CSVFormat) Proxy.newProxyInstance(
                CSVFormat.class.getClassLoader(),
                new Class<?>[] { CSVFormat.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("format".equals(method.getName()) && args != null && args.length == 1) {
                            Object arg = args[0];
                            Object[] values;
                            if (arg instanceof Object[]) {
                                values = (Object[]) arg;
                            } else {
                                values = new Object[] { arg };
                            }
                            try {
                                printer.printRecord(values);
                                return ""; // Since StringWriter is not used here, just return empty string
                            } catch (IOException e) {
                                throw new IllegalStateException(e);
                            }
                        }
                        // Delegate other methods to the original CSVFormat instance
                        return method.invoke(delegate, args);
                    }
                });
    }

    @Test
    @Timeout(8000)
    public void testFormat_withDifferentObjectTypes() {
        Object[] values = new Object[] {123, 45.67, true, 'x'};

        String result = csvFormat.format(values);

        assertEquals("\"123\",\"45.67\",\"true\",\"x\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withSingleValue() {
        Object[] values = new Object[] {"single"};

        String result = csvFormat.format(values);

        assertEquals("\"single\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withSpecialCharacters() {
        Object[] values = new Object[] {"a,b", "c\"d", "e\nf"};

        String result = csvFormat.format(values);

        // Values with comma, quote, and newline should be quoted and escaped accordingly
        assertEquals("\"a,b\",\"c\"\"d\",\"e\nf\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withTrimmedOutput() {
        // The format method trims the output string
        Object[] values = new Object[] {" a ", " b "};

        String result = csvFormat.format(values);

        // The values are quoted but spaces inside quotes remain
        assertEquals("\" a \",\" b \"", result);
    }

}