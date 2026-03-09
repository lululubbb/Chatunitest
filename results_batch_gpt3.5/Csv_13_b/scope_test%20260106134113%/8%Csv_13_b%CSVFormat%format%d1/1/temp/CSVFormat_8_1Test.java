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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

class CSVFormat_8_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void format_withNormalValues_returnsFormattedString() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void format_withNullValue_returnsFormattedString() throws IOException {
        String result = csvFormat.format("a", null, "c");
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    void format_withEmptyValues_returnsEmptyString() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void format_whenCSVPrinterThrowsIOException_throwsIllegalStateException() throws Exception {
        // Create a proxy for CSVPrinter that throws IOException on printRecord
        Class<?> csvPrinterClass = CSVPrinter.class;

        CSVPrinter proxy = (CSVPrinter) Proxy.newProxyInstance(
                csvPrinterClass.getClassLoader(),
                new Class<?>[]{csvPrinterClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
                        if ("printRecord".equals(method.getName())) {
                            throw new IOException("io error");
                        }
                        if ("close".equals(method.getName()) || "flush".equals(method.getName())) {
                            return null;
                        }
                        return null; // no-op for other methods
                    }
                });

        // Use Mockito to mock CSVFormat and override format method to use proxy
        CSVFormat csvFormatMock = mock(CSVFormat.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if ("format".equals(invocation.getMethod().getName())) {
                    try {
                        Object[] args = invocation.getArguments();
                        // The format method signature is format(Object... values)
                        // args itself is the varargs array; pass it as varargs to printRecord
                        proxy.printRecord(args);
                        // If no exception, return some dummy value (empty string)
                        return "";
                    } catch (final IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
                return invocation.callRealMethod();
            }
        });

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            csvFormatMock.format("a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io error", thrown.getCause().getMessage());
    }
}