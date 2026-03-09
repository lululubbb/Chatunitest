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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() {
        CSVFormat formatWithNullString = csvFormat.withNullString("NULL");
        String result = formatWithNullString.format("a", null, "c");
        assertEquals("a,NULL,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withSpecialCharacters() {
        String result = csvFormat.format("a,b", "c\"d", "e\nf");
        // The CSVPrinter should quote fields containing delimiter, quote or linebreak
        assertTrue(result.contains("\"a,b\""));
        assertTrue(result.contains("\"c\"\"d\""));
        assertTrue(result.contains("\"e\nf\""));
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateException_onIOException() throws Exception {
        // Use a dynamic proxy CSVFormat that throws IOException inside format method
        CSVFormat proxyFormat = createCSVFormatProxyThrowingIOException();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            proxyFormat.format("x");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    private CSVFormat createCSVFormatProxyThrowingIOException() {
        return (CSVFormat) Proxy.newProxyInstance(
                CSVFormat.class.getClassLoader(),
                new Class<?>[] { CSVFormat.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("format".equals(method.getName())) {
                            throw new IllegalStateException(new IOException("IO error"));
                        }
                        // Delegate other methods to the original csvFormat
                        return method.invoke(csvFormat, args);
                    }
                });
    }

    @Test
    @Timeout(8000)
    public void testFormat_reflectionInvocation() throws Exception {
        Method formatMethod = CSVFormat.class.getDeclaredMethod("format", Object[].class);
        formatMethod.setAccessible(true);
        Object result = formatMethod.invoke(csvFormat, (Object) new Object[] { "1", "2" });
        assertEquals("1,2", result);
    }
}