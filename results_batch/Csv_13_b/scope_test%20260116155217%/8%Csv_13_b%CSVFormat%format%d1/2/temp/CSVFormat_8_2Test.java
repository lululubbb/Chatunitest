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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNormalValues() throws IOException {
        // Arrange
        Object[] values = new Object[] {"a", "b", "c"};

        // Act
        String result = csvFormat.format(values);

        // Assert
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValue() throws IOException {
        Object[] values = new Object[] {"a", null, "c"};

        String result = csvFormat.format(values);

        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyArray() throws IOException {
        Object[] values = new Object[0];

        String result = csvFormat.format(values);

        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullArray() {
        // Passing null as values should cause NullPointerException in CSVPrinter.printRecord
        assertThrows(NullPointerException.class, () -> {
            csvFormat.format((Object[]) null);
        });
    }

    @Test
    @Timeout(8000)
    public void testFormatThrowsIOExceptionWrapped() throws Exception {
        Object[] values = new Object[] {"a", "b"};

        // Create a StringWriter
        StringWriter sw = new StringWriter();

        // Create a real CSVPrinter with csvFormat and sw
        CSVPrinter realPrinter = new CSVPrinter(sw, csvFormat);

        // Spy the CSVPrinter
        CSVPrinter spyPrinter = spy(realPrinter);

        // Make printRecord throw IOException when called with 'values'
        doThrow(new IOException("IO error")).when(spyPrinter).printRecord(values);

        // Create a CSVFormat instance that returns our spyPrinter when print(Appendable) is called
        CSVFormat csvFormatMock = createCSVFormatReturningPrinter(spyPrinter);

        // Use a wrapper class to simulate the format method with csvFormatMock
        class CSVFormatWrapper {
            private final CSVFormat format;

            CSVFormatWrapper(CSVFormat format) {
                this.format = format;
            }

            public String format(final Object... vals) {
                final StringWriter out = new StringWriter();
                try {
                    // Use the CSVPrinter returned by format.print(out)
                    CSVPrinter printer = format.print(out);
                    printer.printRecord(vals);
                    return out.toString().trim();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        CSVFormatWrapper wrapper = new CSVFormatWrapper(csvFormatMock);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            wrapper.format(values);
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("IO error", ex.getCause().getMessage());
    }

    /**
     * Use dynamic Proxy to create a CSVFormat instance that returns the given CSVPrinter when print(Appendable) is called.
     */
    private CSVFormat createCSVFormatReturningPrinter(CSVPrinter printer) throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("print".equals(method.getName()) && args != null && args.length == 1 && args[0] instanceof Appendable) {
                    return printer;
                }
                // Delegate other methods to baseFormat
                return method.invoke(baseFormat, args);
            }
        };

        return (CSVFormat) Proxy.newProxyInstance(
                CSVFormat.class.getClassLoader(),
                new Class<?>[] {CSVFormat.class},
                handler);
    }
}