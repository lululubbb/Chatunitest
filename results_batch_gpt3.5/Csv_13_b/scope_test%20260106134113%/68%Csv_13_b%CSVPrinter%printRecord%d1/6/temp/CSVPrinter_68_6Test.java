package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_68_6Test {

    private Appendable appendable;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        appendable = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Object[] values = {"a", 123, null, "test"};

        printer.printRecord(values);

        String output = appendable.toString();
        assertTrue(output.endsWith(System.lineSeparator()) || output.endsWith("\n") || output.endsWith("\r\n"));
        for (Object val : values) {
            if (val != null) {
                assertTrue(output.contains(val.toString()));
            }
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyArray() throws IOException {
        Object[] values = new Object[0];
        printer.printRecord(values);
        String output = appendable.toString();
        assertTrue(output.endsWith(System.lineSeparator()) || output.endsWith("\n") || output.endsWith("\r\n"));
        assertEquals("", output.trim());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        Object[] values = {null};
        printer.printRecord(values);
        String output = appendable.toString();
        assertTrue(output.endsWith(System.lineSeparator()) || output.endsWith("\n") || output.endsWith("\r\n"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(appendable, format));
        Object[] values = {"one", "two"};

        spyPrinter.printRecord(values);

        for (Object val : values) {
            verify(spyPrinter).print(val);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvocation() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        method.setAccessible(true);
        method.invoke(printer, (Object) new Object[] {"val1", "val2"});

        String output = appendable.toString();
        assertTrue(output.contains("val1"));
        assertTrue(output.contains("val2"));
    }
}