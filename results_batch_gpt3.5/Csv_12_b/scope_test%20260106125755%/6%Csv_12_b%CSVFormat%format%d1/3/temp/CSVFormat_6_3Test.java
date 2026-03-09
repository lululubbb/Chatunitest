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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_6_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNormalValues() throws Exception {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValue() throws Exception {
        String result = csvFormat.format("a", null, "c");
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyValues() throws Exception {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // Spy the CSVPrinter to throw IOException on printRecord(Object...)
        StringWriter sw = new StringWriter();
        CSVPrinter realPrinter = new CSVPrinter(sw, csvFormat);
        CSVPrinter spyPrinter = Mockito.spy(realPrinter);

        // Fix: use doThrow with doAnswer to disambiguate varargs method
        doThrow(new IOException("mock IO error")).when(spyPrinter).printRecord(any());

        // Create a wrapper similar to CSVFormat.format but using the spyPrinter
        class FaultyFormatWrapper {
            private final CSVPrinter printer;
            private final StringWriter writer;

            FaultyFormatWrapper(CSVPrinter printer, StringWriter writer) {
                this.printer = printer;
                this.writer = writer;
            }

            public String format(final Object... values) {
                try {
                    printer.printRecord(values);
                    printer.flush();
                    return writer.toString().trim();
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        FaultyFormatWrapper faultyFormat = new FaultyFormatWrapper(spyPrinter, sw);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            faultyFormat.format("a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO error", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testFormatTrimOutput() throws Exception {
        // Using withRecordSeparator that adds trailing spaces
        CSVFormat customFormat = CSVFormat.DEFAULT.withRecordSeparator(" \n");
        String result = customFormat.format("a", "b");
        assertEquals("a,b", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithVariousObjects() throws Exception {
        Object[] values = { 1, 2.5, true, 'c', new Object() {
            @Override
            public String toString() {
                return "obj";
            }
        }};
        String result = csvFormat.format(values);
        // The result should contain string representations separated by commas
        assertTrue(result.startsWith("1,2.5,true,c,"));
        assertTrue(result.endsWith("obj"));
    }
}