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

class CSVFormat_7_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormatWithNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithEmptyValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithNullValues() throws IOException {
        String result = csvFormat.format((Object) null, "test", null);
        assertEquals(",test,", result);
    }

    @Test
    @Timeout(8000)
    void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // Create a spy CSVPrinter that throws IOException on printRecord
        CSVPrinter spyPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("mocked IO exception")).when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).close();

        // Use reflection to create a CSVFormat instance (using the public DEFAULT as base)
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a dynamic proxy to override print method to return our spyPrinter
        CSVFormat proxyFormat = (CSVFormat) Proxy.newProxyInstance(
                CSVFormat.class.getClassLoader(),
                new Class<?>[]{CSVFormat.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("print".equals(method.getName()) && args != null && args.length == 1) {
                            return spyPrinter;
                        }
                        return method.invoke(format, args);
                    }
                });

        // Since CSVFormat is a final class and cannot be proxied easily,
        // we use a subclass of CSVFormat via reflection to override print method.
        // But as this is not possible, instead we create a subclass of CSVPrinter to simulate the behavior.

        // Instead, use a small workaround: create a CSVFormat instance and override printRecord via a spy on CSVPrinter.

        // So we test the format method by mocking CSVPrinter constructor to return our spyPrinter.

        // To do this, use Mockito's ability to mock constructors via mockito-inline or PowerMock is needed,
        // but here we simulate by testing the exception thrown by format method indirectly.

        // Instead, we create a helper class to simulate CSVPrinter creation and throw IOException on printRecord.

        // Because we cannot extend CSVFormat or mock its constructor, we test the exception by
        // creating a subclass of CSVPrinter that throws IOException on printRecord and
        // then inject it via a custom CSVFormat instance.

        // So we test format method by reflection invoking private method printRecord to throw IOException.

        // Alternatively, test the IllegalStateException wrapping IOException by directly calling format with a CSVPrinter mock.

        // Since the above approaches are complicated and the original approach is invalid due to final class,
        // we test the exception by a small helper class that extends CSVPrinter and throws IOException.

        // Create CSVFormat that returns a CSVPrinter that throws IOException on printRecord via a small hack:

        CSVFormat formatWithThrowingPrinter = new CSVFormatThrowingPrinter(format, spyPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> formatWithThrowingPrinter.format("x"));
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mocked IO exception", thrown.getCause().getMessage());
    }

    // Helper class that delegates all CSVFormat calls except print() which returns the spyPrinter
    private static class CSVFormatThrowingPrinter extends CSVFormat {
        private final CSVFormat delegate;
        private final CSVPrinter throwingPrinter;

        CSVFormatThrowingPrinter(CSVFormat delegate, CSVPrinter throwingPrinter) throws Exception {
            // Use reflection to call CSVFormat's private constructor with all fields copied
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, Class.forName("org.apache.commons.csv.CSVFormat$QuoteMode"),
                    Character.class, Character.class, boolean.class, boolean.class,
                    String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            this.delegate = delegate;
            this.throwingPrinter = throwingPrinter;
            // Create new CSVFormat instance with same parameters
            CSVFormat instance = constructor.newInstance(
                    delegate.getDelimiter(),
                    delegate.getQuoteCharacter(),
                    delegate.getQuoteMode(),
                    delegate.getCommentMarker(),
                    delegate.getEscapeCharacter(),
                    delegate.getIgnoreSurroundingSpaces(),
                    delegate.getIgnoreEmptyLines(),
                    delegate.getRecordSeparator(),
                    delegate.getNullString(),
                    delegate.getHeaderComments(),
                    delegate.getHeader(),
                    delegate.getSkipHeaderRecord(),
                    delegate.getAllowMissingColumnNames(),
                    delegate.getIgnoreHeaderCase(),
                    delegate.getTrim(),
                    delegate.getTrailingDelimiter());
            // Copy all fields from instance to this (via reflection)
            for (java.lang.reflect.Field field : CSVFormat.class.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(this, field.get(instance));
            }
        }

        @Override
        public CSVPrinter print(final Appendable out) throws IOException {
            return throwingPrinter;
        }
    }
}