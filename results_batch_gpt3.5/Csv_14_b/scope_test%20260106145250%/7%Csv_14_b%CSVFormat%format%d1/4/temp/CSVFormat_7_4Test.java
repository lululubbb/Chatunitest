package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.charset.Charset;
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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_7_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withMultipleValues() throws IOException {
        String[] values = {"value1", "value2", "value3"};
        String result = csvFormat.format((Object[]) values);
        assertNotNull(result);
        // The default delimiter is comma, default quote is double quote, default record separator CRLF
        // So output should be: value1,value2,value3 (trimmed)
        assertEquals("value1,value2,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValues() {
        Object[] values = {null, "text", null};
        String result = csvFormat.format(values);
        assertNotNull(result);
        // Null values are printed as empty strings by default
        assertEquals(",text,", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() {
        Object[] values = {};
        String result = csvFormat.format(values);
        assertNotNull(result);
        // No values means empty string
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withSingleValue() {
        Object[] values = {"singleValue"};
        String result = csvFormat.format(values);
        assertNotNull(result);
        assertEquals("singleValue", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Use reflection and proxy to mock CSVPrinter constructor and make printRecord throw IOException
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);

        Appendable appendable = new StringWriter();

        // Create a proxy instance of CSVPrinter that throws IOException on printRecord
        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class},
                new InvocationHandler() {
                    private final CSVPrinter realPrinter = constructor.newInstance(appendable, CSVFormat.DEFAULT);
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("printRecord".equals(method.getName())) {
                            throw new IOException("IO error");
                        }
                        if ("close".equals(method.getName())) {
                            return null;
                        }
                        return method.invoke(realPrinter, args);
                    }
                });

        // Use a spy with Mockito to override print(Appendable) method to return our proxyPrinter
        CSVFormat spyFormat = spy(csvFormat);
        doReturn(proxyPrinter).when(spyFormat).print(any(Appendable.class));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            spyFormat.format("value");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }
}