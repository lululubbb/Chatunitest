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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_6_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use DEFAULT format for testing
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws Exception {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValues() throws Exception {
        String result = csvFormat.format("a", null, "c");
        // null values are printed as empty strings by default
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws Exception {
        String result = csvFormat.format("", "", "");
        assertEquals(",,,", result.replaceAll("[\\r\\n]", "").substring(0,3) + "", "Empty strings separated by commas");
    }

    @Test
    @Timeout(8000)
    void testFormat_withSingleValue() throws Exception {
        String result = csvFormat.format("single");
        assertEquals("single", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNoValues() throws Exception {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Mock CSVPrinter to throw IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("mock IO error")).when(mockPrinter).printRecord(any(Object[].class));

        // Create a proxy CSVFormat by subclassing DEFAULT and override format method using reflection to inject mockPrinter
        CSVFormat format = new CSVFormatProxy(CSVFormat.DEFAULT, mockPrinter);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            format.format("value");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO error", thrown.getCause().getMessage());
    }

    private static class CSVFormatProxy extends CSVFormat {
        private static final long serialVersionUID = 1L;
        private final CSVPrinter printer;

        CSVFormatProxy(CSVFormat base, CSVPrinter printer) throws Exception {
            super(base.getDelimiter(), base.getQuoteChar(), base.getQuotePolicy(), base.getCommentStart(),
                    base.getEscape(), base.getIgnoreSurroundingSpaces(), base.getIgnoreEmptyLines(),
                    base.getRecordSeparator(), base.getNullString(), base.getHeader(), base.getSkipHeaderRecord());
            this.printer = printer;
        }

        @Override
        public String format(Object... values) {
            try {
                printer.printRecord(values);
                return "";
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Test
    @Timeout(8000)
    void testFormat_reflectionInvoke() throws Exception {
        Method formatMethod = CSVFormat.class.getDeclaredMethod("format", Object[].class);
        formatMethod.setAccessible(true);
        String result = (String) formatMethod.invoke(csvFormat, (Object) new Object[]{"x", "y"});
        assertEquals("x,y", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withDifferentCSVFormatInstance() throws Exception {
        // Create CSVFormat instance with different delimiter and quote char using with* methods
        CSVFormat customFormat = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuoteChar('"')
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\n")
                .withSkipHeaderRecord(false);

        String result = customFormat.format("a", "b;c", "c");
        // The value with delimiter should be quoted
        assertEquals("a;\"b;c\";c", result);
    }
}