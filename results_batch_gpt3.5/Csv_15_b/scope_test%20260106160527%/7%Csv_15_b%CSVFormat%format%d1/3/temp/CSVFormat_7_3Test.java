package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

public class CSVFormat_7_3Test {

    @Test
    @Timeout(8000)
    void testFormat_withMultipleValues() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        String result = csvFormat.format("value1", "value2", "value3");

        // Default delimiter is comma, default quote is double quote, default record separator CRLF,
        // so expected output is "value1,value2,value3" without trailing CRLF (trimmed)
        assertEquals("value1,value2,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValue() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString("NULL");

        String result = csvFormat.format("value1", null, "value3");

        assertEquals("value1,NULL,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        String result = csvFormat.format();

        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateException_onIOException() throws IOException {
        // Use Proxy to mock CSVPrinter to throw IOException when printRecord is called,
        // since mockConstruction is not available
        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class, AutoCloseable.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("printRecord".equals(method.getName())) {
                            throw new IOException("IO error");
                        }
                        if ("close".equals(method.getName())) {
                            return null;
                        }
                        if ("flush".equals(method.getName())) {
                            return null;
                        }
                        Class<?> returnType = method.getReturnType();
                        if (returnType.isPrimitive()) {
                            if (returnType == boolean.class) {
                                return false;
                            } else if (returnType == void.class) {
                                return null;
                            } else if (returnType == byte.class) {
                                return (byte) 0;
                            } else if (returnType == short.class) {
                                return (short) 0;
                            } else if (returnType == int.class) {
                                return 0;
                            } else if (returnType == long.class) {
                                return 0L;
                            } else if (returnType == float.class) {
                                return 0f;
                            } else if (returnType == double.class) {
                                return 0d;
                            } else if (returnType == char.class) {
                                return '\0';
                            }
                        }
                        return null;
                    }
                });

        // Spy CSVFormat.DEFAULT and override print(Appendable) to return proxyPrinter
        CSVFormat csvFormatWithMockPrinter = spy(CSVFormat.DEFAULT);

        try {
            Method printMethod = CSVFormat.class.getDeclaredMethod("print", Appendable.class);
            printMethod.setAccessible(true);
            doReturn(proxyPrinter).when(csvFormatWithMockPrinter).print(any(Appendable.class));
        } catch (NoSuchMethodException e) {
            fail("Method print(Appendable) not found in CSVFormat");
        }

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            csvFormatWithMockPrinter.format("value1");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }
}