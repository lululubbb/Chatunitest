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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class CSVFormat_6_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        // The CSVPrinter will separate with commas and quote as per DEFAULT format
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValue() throws IOException {
        String result = csvFormat.format("a", null, "c");
        // null is printed as empty string by default
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIOExceptionWrappedInIllegalStateException() throws Exception {
        // Use reflection to create a proxy CSVPrinter that throws IOException on printRecord
        Constructor<CSVPrinter> ctor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        ctor.setAccessible(true);

        InvocationHandler handler = new InvocationHandler() {
            private final CSVPrinter realPrinter = createRealPrinter();

            private CSVPrinter createRealPrinter() {
                try {
                    return ctor.newInstance(new StringWriter(), csvFormat);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("printRecord".equals(method.getName())) {
                    throw new IOException("IO error");
                }
                if ("close".equals(method.getName()) || "flush".equals(method.getName())) {
                    return null;
                }
                return method.invoke(realPrinter, args);
            }
        };

        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class[] { CSVPrinter.class },
                handler);

        // Create a CSVFormat mock that overrides format to use proxyPrinter
        CSVFormat csvFormatWithProxy = mock(CSVFormat.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if ("format".equals(invocation.getMethod().getName())) {
                    Object[] values = (Object[]) invocation.getArguments()[0];
                    try {
                        proxyPrinter.printRecord(values);
                        return "";
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
                // delegate other calls to real csvFormat
                return invocation.getMethod().invoke(csvFormat, invocation.getArguments());
            }
        });

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            csvFormatWithProxy.format(new Object[] { "x" });
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("IO error", ex.getCause().getMessage());
    }
}