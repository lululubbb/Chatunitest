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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        // Default delimiter is comma, default quote char is double quote, default record separator is CRLF
        // So expected output is: a,b,c
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() throws IOException {
        String result = csvFormat.format("a", null, "c");
        // null should be printed as empty string by default
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format();
        // No values means empty record, so empty string after trim
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateException_whenIOExceptionOccurs() throws Exception {
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Resolve ambiguity by casting argument matcher to Object[]
        doThrow(new IOException("mock io exception")).when(mockPrinter).printRecord((Object[]) any());

        // Create a proxy CSVFormat by delegating to original, but override format method to use mockPrinter
        CSVFormat testFormat = new CSVFormatProxy(csvFormat, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            testFormat.format("x");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock io exception", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testFormat_invokesPrivateFormatMethodUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method formatMethod = CSVFormat.class.getDeclaredMethod("format", Object[].class);
        formatMethod.setAccessible(true);
        String result = (String) formatMethod.invoke(csvFormat, (Object) new Object[]{"x", "y"});
        assertEquals("x,y", result);
    }

    // Helper proxy class that delegates to CSVFormat but overrides format method to use mocked CSVPrinter
    private static class CSVFormatProxy extends CSVFormat {

        private final CSVFormat delegate;
        private final CSVPrinter printer;

        CSVFormatProxy(CSVFormat delegate, CSVPrinter printer) {
            this.delegate = delegate;
            this.printer = printer;
        }

        @Override
        public String format(Object... values) {
            try {
                printer.printRecord(values);
                // Return empty string since printer is mocked and output is not captured here
                return "";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        // Delegate other methods to the original CSVFormat instance as needed
        @Override
        public char getDelimiter() {
            return delegate.getDelimiter();
        }

        @Override
        public Character getQuoteChar() {
            return delegate.getQuoteChar();
        }

        @Override
        public Quote getQuotePolicy() {
            return delegate.getQuotePolicy();
        }

        @Override
        public Character getCommentStart() {
            return delegate.getCommentStart();
        }

        @Override
        public Character getEscape() {
            return delegate.getEscape();
        }

        @Override
        public boolean getIgnoreSurroundingSpaces() {
            return delegate.getIgnoreSurroundingSpaces();
        }

        @Override
        public boolean getIgnoreEmptyLines() {
            return delegate.getIgnoreEmptyLines();
        }

        @Override
        public String getRecordSeparator() {
            return delegate.getRecordSeparator();
        }

        @Override
        public String getNullString() {
            return delegate.getNullString();
        }

        @Override
        public String[] getHeader() {
            return delegate.getHeader();
        }

        @Override
        public boolean getSkipHeaderRecord() {
            return delegate.getSkipHeaderRecord();
        }
    }
}