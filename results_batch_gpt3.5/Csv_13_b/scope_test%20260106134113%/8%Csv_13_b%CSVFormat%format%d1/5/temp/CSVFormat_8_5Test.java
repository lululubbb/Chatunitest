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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

public class CSVFormat_8_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() throws Exception {
        CSVFormat spyFormat = spy(csvFormat);

        // Mock the print(Appendable) method to return a CSVPrinter that does nothing on printRecord
        doAnswer((InvocationOnMock invocation) -> {
            Appendable out = (Appendable) invocation.getArguments()[0];
            return createCSVPrinterProxy((values) -> {
                // do nothing on printRecord
            }, out);
        }).when(spyFormat).print(any(Appendable.class));

        String result = callFormatWithMockPrinter(spyFormat, "a", "b", "c");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues() throws Exception {
        CSVFormat spyFormat = spy(csvFormat);

        doAnswer((InvocationOnMock invocation) -> {
            Appendable out = (Appendable) invocation.getArguments()[0];
            return createCSVPrinterProxy((values) -> {
                // do nothing on printRecord
            }, out);
        }).when(spyFormat).print(any(Appendable.class));

        String result = callFormatWithMockPrinter(spyFormat, (Object) null);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIOException() throws Exception {
        CSVFormat spyFormat = spy(csvFormat);

        doAnswer((InvocationOnMock invocation) -> {
            Appendable out = (Appendable) invocation.getArguments()[0];
            return createCSVPrinterProxy((values) -> {
                throw new IOException("IO error");
            }, out);
        }).when(spyFormat).print(any(Appendable.class));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            callFormatWithMockPrinter(spyFormat, "value");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }

    // Helper interface for lambda to handle printRecord calls
    @FunctionalInterface
    interface PrintRecordHandler {
        void handle(Object[] values) throws IOException;
    }

    // Create a proxy CSVPrinter that intercepts printRecord calls
    private CSVPrinter createCSVPrinterProxy(PrintRecordHandler handler, Appendable out) throws Exception {
        CSVFormat dummyFormat = CSVFormat.DEFAULT;
        CSVPrinter realPrinter = new CSVPrinter(out, dummyFormat);

        return (CSVPrinter) java.lang.reflect.Proxy.newProxyInstance(
                CSVPrinter.class.getClassLoader(),
                new Class<?>[]{CSVPrinter.class},
                (proxy, method, args) -> {
                    if ("printRecord".equals(method.getName())) {
                        handler.handle(args);
                        return null;
                    }
                    return method.invoke(realPrinter, args);
                });
    }

    // Call the format method on CSVFormat but inject our proxy CSVPrinter
    private String callFormatWithMockPrinter(CSVFormat format, Object... values) throws Exception {
        return format.format(values);
    }
}