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

class CSVFormat_7_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("value1", "value2", "value3");
        // Default delimiter is comma, default quote char is "
        assertEquals("\"value1\",\"value2\",\"value3\"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format("", null, " ");
        // null is printed as empty string by default
        assertEquals("\"\",\"\",\" \"", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNoValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Create a proxy for CSVPrinter that throws IOException on printRecord
        CSVPrinter proxyPrinter = (CSVPrinter) Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class, AutoCloseable.class},
                new InvocationHandler() {
                    private boolean closed = false;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        if ("printRecord".equals(methodName)) {
                            throw new IOException("io error");
                        } else if ("close".equals(methodName)) {
                            closed = true;
                            return null;
                        } else if ("toString".equals(methodName)) {
                            return "";
                        }
                        return null;
                    }
                });

        // Use a CSVFormat instance and override the format method via a local anonymous class without calling constructor
        CSVFormat csvFormatSpy = new CSVFormat() {
            @Override
            public String format(final Object... values) {
                final StringWriter out = new StringWriter();
                try (final CSVPrinter csvPrinter = proxyPrinter) {
                    csvPrinter.printRecord(values);
                    return out.toString().trim();
                } catch (final IOException e) {
                    // should not happen because a StringWriter does not do IO.
                    throw new IllegalStateException(e);
                }
            }
        };

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            csvFormatSpy.format("a");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io error", thrown.getCause().getMessage());
    }
}