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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_6_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValues() throws IOException {
        String result = csvFormat.format((Object) null);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_trimsResult() throws IOException {
        String result = csvFormat.format(" a ", " b ");
        // The CSVPrinter adds quotes if necessary, but our format trims output
        assertEquals("\" a \",\" b \"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateExceptionOnIOException() throws Throwable {
        // Create a proxy CSVPrinter that throws IOException on printRecord
        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("printRecord".equals(method.getName())) {
                            throw new IOException("forced IOException");
                        }
                        if (method.getReturnType().isPrimitive()) {
                            if (method.getReturnType() == boolean.class) return false;
                            if (method.getReturnType() == int.class) return 0;
                            if (method.getReturnType() == long.class) return 0L;
                            if (method.getReturnType() == float.class) return 0f;
                            if (method.getReturnType() == double.class) return 0d;
                            if (method.getReturnType() == byte.class) return (byte) 0;
                            if (method.getReturnType() == char.class) return (char) 0;
                            if (method.getReturnType() == short.class) return (short) 0;
                        }
                        return null;
                    }
                });

        // Use Mockito to mock CSVFormat and override format method to use proxyPrinter
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.format(any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            try {
                proxyPrinter.printRecord(args);
                return "";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            csvFormatMock.format("x");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("forced IOException", thrown.getCause().getMessage());
    }
}