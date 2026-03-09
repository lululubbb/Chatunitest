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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

public class CSVFormat_7_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithMultipleValues() {
        String result = csvFormat.format("value1", "value2", 123, null);
        // The output should be a CSV line with the values comma separated and trimmed
        assertEquals("value1,value2,123,", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValue() {
        String result = csvFormat.format((Object) null);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // Use reflection to create a proxy CSVPrinter that throws IOException on printRecord
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Create a CSVFormat spy that returns a proxy CSVPrinter throwing IOException on printRecord
        CSVFormat csvFormatSpy = spy(csvFormat);

        doAnswer(invocation -> {
            Appendable out = invocation.getArgument(0);
            CSVPrinter originalPrinter = constructor.newInstance(out, csvFormatSpy);

            CSVPrinter proxy = (CSVPrinter) Proxy.newProxyInstance(
                    CSVPrinter.class.getClassLoader(),
                    new Class[]{CSVPrinter.class, AutoCloseable.class},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("printRecord".equals(method.getName())) {
                                throw new IOException("IO error");
                            }
                            if ("close".equals(method.getName())) {
                                // do nothing to avoid exceptions
                                return null;
                            }
                            return method.invoke(originalPrinter, args);
                        }
                    });
            return proxy;
        }).when(csvFormatSpy).print(any(Appendable.class));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            csvFormatSpy.format("test");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }
}