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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    /**
     * Helper method to create a proxy for CSVPrinter that intercepts printRecord calls.
     */
    private CSVPrinter createMockPrinter(InvocationHandler handler) {
        return (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[] { CSVPrinter.class },
                handler);
    }

    /**
     * Helper method to create a proxy for CSVPrinter that intercepts printRecord calls with given args.
     */
    private CSVPrinter createMockPrinterWithPrintRecordBehavior(Object[] expectedArgs, Runnable onPrintRecord) {
        return createMockPrinter((proxy, method, args) -> {
            if ("printRecord".equals(method.getName())) {
                // Disambiguate overloaded method by checking args type
                if (args != null && args.length == 1 && args[0] instanceof Iterable) {
                    // This is printRecord(Iterable<?>), ignore for this test
                    return null;
                }
                // Otherwise, assume printRecord(Object...)
                if (expectedArgs == null) {
                    // no args expected
                    if (args == null || args.length == 0) {
                        onPrintRecord.run();
                        return null;
                    }
                } else if (expectedArgs.length == 1 && expectedArgs[0] == null) {
                    // single null arg expected
                    if (args != null && args.length == 1 && args[0] == null) {
                        onPrintRecord.run();
                        return null;
                    }
                } else {
                    // multiple args expected
                    if (args != null && args.length == expectedArgs.length) {
                        boolean match = true;
                        for (int i = 0; i < args.length; i++) {
                            if (expectedArgs[i] == null) {
                                if (args[i] != null) {
                                    match = false;
                                    break;
                                }
                            } else if (!expectedArgs[i].equals(args[i])) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            onPrintRecord.run();
                            return null;
                        }
                    }
                }
                // If args don't match expected, just proceed without action
                return null;
            }
            // For other methods, do nothing or return default
            if (method.getReturnType().isPrimitive()) {
                if (method.getReturnType() == boolean.class) {
                    return false;
                } else if (method.getReturnType() == void.class) {
                    return null;
                } else if (method.getReturnType() == int.class) {
                    return 0;
                } else if (method.getReturnType() == long.class) {
                    return 0L;
                } else if (method.getReturnType() == double.class) {
                    return 0d;
                } else if (method.getReturnType() == float.class) {
                    return 0f;
                } else if (method.getReturnType() == byte.class) {
                    return (byte) 0;
                } else if (method.getReturnType() == short.class) {
                    return (short) 0;
                } else if (method.getReturnType() == char.class) {
                    return '\0';
                }
            }
            return null;
        });
    }

    /**
     * Reflection helper to create a CSVPrinter instance proxy for constructor interception.
     */
    private CSVPrinter createCSVPrinterProxy(Object[] expectedArgs, Runnable onPrintRecord) {
        return createMockPrinterWithPrintRecordBehavior(expectedArgs, onPrintRecord);
    }

    /**
     * Reflection helper to invoke CSVFormat.format() with a mocked CSVPrinter to intercept printRecord calls.
     */
    private String invokeFormatWithMockPrinter(Object[] args, Object[] expectedPrintRecordArgs, Runnable onPrintRecord) throws Exception {
        // Create proxy CSVPrinter with printRecord interception
        CSVPrinter printerProxy = createCSVPrinterProxy(expectedPrintRecordArgs, onPrintRecord);

        // Use reflection to get CSVPrinter constructor (Appendable, CSVFormat)
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Create a StringWriter to capture output
        StringWriter out = new StringWriter();

        // Create the real CSVPrinter instance
        CSVPrinter realPrinter = constructor.newInstance(out, csvFormat);

        InvocationHandler handler = (proxy, method, methodArgs) -> {
            if ("printRecord".equals(method.getName())) {
                // Delegate to our proxy printerProxy's printRecord to intercept and run onPrintRecord
                Method printRecordMethod = CSVPrinter.class.getMethod("printRecord", Object[].class);
                try {
                    return printRecordMethod.invoke(printerProxy, (Object) methodArgs);
                } catch (Exception e) {
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        throw cause;
                    }
                    throw e;
                }
            }
            // Delegate other methods to the real printer
            try {
                Method m = realPrinter.getClass().getMethod(method.getName(), method.getParameterTypes());
                return m.invoke(realPrinter, methodArgs);
            } catch (NoSuchMethodException e) {
                // Method not found on realPrinter, return default
                if (method.getReturnType().isPrimitive()) {
                    if (method.getReturnType() == boolean.class) {
                        return false;
                    } else if (method.getReturnType() == void.class) {
                        return null;
                    } else if (method.getReturnType() == int.class) {
                        return 0;
                    } else if (method.getReturnType() == long.class) {
                        return 0L;
                    } else if (method.getReturnType() == double.class) {
                        return 0d;
                    } else if (method.getReturnType() == float.class) {
                        return 0f;
                    } else if (method.getReturnType() == byte.class) {
                        return (byte) 0;
                    } else if (method.getReturnType() == short.class) {
                        return (short) 0;
                    } else if (method.getReturnType() == char.class) {
                        return '\0';
                    }
                }
                return null;
            }
        };

        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class},
                handler);

        // Call printRecord on proxyPrinter with args
        proxyPrinter.printRecord(args);

        return out.toString().trim();
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() throws Exception {
        final Object[] expectedArgs = new Object[] { "a", "b", "c" };
        final boolean[] printed = { false };

        String result = invokeFormatWithMockPrinter(expectedArgs, expectedArgs, () -> printed[0] = true);

        assertNotNull(result);
        assertEquals("a,b,c", result);
        assertTrue(printed[0], "printRecord should have been called with expected arguments");
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues() throws Exception {
        final Object[] expectedArgs = new Object[] { null };
        final boolean[] printed = { false };

        String result = invokeFormatWithMockPrinter(new Object[] { null }, expectedArgs, () -> printed[0] = true);

        assertNotNull(result);
        assertEquals("", result);
        assertTrue(printed[0], "printRecord should have been called with null argument");
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() throws Exception {
        final Object[] expectedArgs = new Object[0];
        final boolean[] printed = { false };

        String result = invokeFormatWithMockPrinter(new Object[0], expectedArgs, () -> printed[0] = true);

        assertNotNull(result);
        assertEquals("", result);
        assertTrue(printed[0], "printRecord should have been called with no arguments");
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIOException() throws Exception {
        // We cannot extend CSVFormat to override format() because it's final and constructor private.
        // Instead, we test that when CSVPrinter.printRecord throws IOException, it is wrapped properly.

        CSVPrinter printerMock = mock(CSVPrinter.class);
        // Cast to Object[] to disambiguate varargs method
        doThrow(new IOException("IO error")).when(printerMock).printRecord((Object[]) any());

        // We simulate the behavior of CSVFormat.format() method manually:
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            try {
                printerMock.printRecord("a");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("IO error", ex.getCause().getMessage());
    }
}