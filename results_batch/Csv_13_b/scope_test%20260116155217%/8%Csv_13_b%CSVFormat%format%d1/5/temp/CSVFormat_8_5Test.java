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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_5Test {

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
        String result = csvFormat.format("a", null, "c");
        // null is printed as empty string by default
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withDifferentCSVFormat() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote((Character) null).withRecordSeparator("\n");
        String result = format.format("a", "b;c", "d");
        assertEquals("a;b;c;d", result); // Because quote is null, delimiter inside field is not quoted
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Create mock CSVPrinter that throws IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("forced IO error")).when(mockPrinter).printRecord((Object[]) any());

        // Create a proxy CSVFormat that overrides print(Appendable) to return mockPrinter
        CSVFormat formatProxy = createCSVFormatProxyReturningPrinter(mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            formatProxy.format("a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("forced IO error", thrown.getCause().getMessage());
    }

    private CSVFormat createCSVFormatProxyReturningPrinter(CSVPrinter printer) throws Exception {
        // Use reflection to get all fields from DEFAULT and create a new CSVFormat instance via with* methods
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat format = CSVFormat.DEFAULT;
        // We use a proxy to override print(Appendable) method
        return (CSVFormat) java.lang.reflect.Proxy.newProxyInstance(
                CSVFormat.class.getClassLoader(),
                new Class[]{CSVFormat.class},
                (proxy, method, args) -> {
                    if ("print".equals(method.getName()) && args.length == 1 && args[0] instanceof Appendable) {
                        return printer;
                    }
                    // delegate all other calls to base
                    return method.invoke(base, args);
                });
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);

        // Test line break characters
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertTrue((Boolean) method.invoke(null, '\r'));
        // Test non line break character
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);

        // Test line break characters
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        // Test null input
        assertFalse((Boolean) method.invoke(null, (Character) null));
        // Test non line break character
        assertFalse((Boolean) method.invoke(null, Character.valueOf('x')));
    }
}